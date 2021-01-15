package com.itachi1706.helperlib.extlib.fingerprint

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.TextView.OnEditorActionListener
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity
import com.afollestad.materialdialogs.DialogAction
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.internal.MDTintHelper
import com.itachi1706.helperlib.R

/**
 * A dialog which uses fingerprint APIs to authenticate the user, and falls back to password
 * authentication if fingerprint is not available.
 *
 */
@Deprecated("Slated for removal soon (some functions may remain)")
class FingerprintDialog : DialogFragment(), OnEditorActionListener, FingerprintLibCallback {
    interface Callback {
        fun onFingerprintDialogAuthenticated()
        fun onFingerprintDialogVerifyPassword(dialog: FingerprintDialog?, password: String?)
        fun onFingerprintDialogStageUpdated(dialog: FingerprintDialog?, stage: Stage?)
        fun onFingerprintDialogCancelled()
    }

    private var mFingerprintContent: View? = null
    private var mBackupContent: View? = null
    private var mPassword: EditText? = null
    private var mUseFingerprintFutureCheckBox: CheckBox? = null
    private var mPasswordDescriptionTextView: TextView? = null
    private var mNewFingerprintEnrolledTextView: TextView? = null
    private var mFingerprintIcon: ImageView? = null
    private var mFingerprintStatus: TextView? = null
    private var mLastStage: Stage? = null
    private var mStage: Stage? = Stage.FINGERPRINT
    private var mFingerprintLib: FingerprintLib? = null
    private var mCallback: Callback? = null
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable("stage", mStage)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        check(!(arguments == null || !requireArguments().containsKey("key_name"))) { "FingerprintDialog must be shown with show(Activity, String, int)." }
        if (savedInstanceState != null) mStage =
            savedInstanceState.getSerializable("stage") as Stage?
        isCancelable = requireArguments().getBoolean("cancelable", true)
        val dialog = MaterialDialog.Builder(requireActivity())
            .title(R.string.sign_in)
            .customView(R.layout.fingerprint_dialog_container, false)
            .positiveText(android.R.string.cancel)
            .negativeText(R.string.use_password)
            .autoDismiss(false)
            .cancelable(requireArguments().getBoolean("cancelable", true))
            .onPositive { materialDialog, dialogAction -> materialDialog.cancel() }
            .onNegative { materialDialog, dialogAction ->
                if (mStage == Stage.FINGERPRINT) {
                    goToBackup(materialDialog)
                } else {
                    verifyPassword()
                }
            }.build()
        val v = dialog.customView!!
        mFingerprintContent = v.findViewById(R.id.fingerprint_container)
        mBackupContent = v.findViewById(R.id.backup_container)
        mPassword = v.findViewById(R.id.password)
        mPassword?.setOnEditorActionListener(this)
        mPasswordDescriptionTextView = v.findViewById(R.id.password_description)
        mUseFingerprintFutureCheckBox = v.findViewById(R.id.use_fingerprint_in_future_check)
        mNewFingerprintEnrolledTextView = v.findViewById(R.id.new_fingerprint_enrolled_description)
        mFingerprintIcon = v.findViewById(R.id.fingerprint_icon)
        mFingerprintStatus = v.findViewById(R.id.fingerprint_status)
        mFingerprintStatus?.setText(R.string.initializing)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateStage(null)
    }

    override fun onResume() {
        super.onResume()
        mFingerprintLib = FingerprintLib.init(
            requireActivity(),
            requireArguments().getString("key_name", ""),
            requireArguments().getInt("request_code", -1),
            this@FingerprintDialog
        )
    }

    override fun onPause() {
        super.onPause()
        if (FingerprintLib.get() != null) FingerprintLib.get().stopListening()
    }

    override fun onCancel(dialog: DialogInterface) {
        super.onCancel(dialog)
        redirectToActivity()
        if (mCallback != null) mCallback!!.onFingerprintDialogCancelled()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        redirectToActivity()
    }

    private fun redirectToActivity() {
        FingerprintLib.deinit()
        if (activity != null &&
            activity is FingerprintLibCallback &&
            requireArguments().getBoolean("was_initialized", false)
        ) {
            FingerprintLib.init(
                requireActivity(),
                requireArguments().getString("key_name", ""),
                requireArguments().getInt("request_code", -1),
                (activity as FingerprintLibCallback?)!!
            )
        }
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        if (activity !is Callback) {
            FingerprintLib.deinit()
            throw IllegalStateException("Activities showing a FingerprintDialog must implement FingerprintDialog.Callback.")
        }
        mCallback = activity
    }

    /**
     * Switches to backup (password) screen. This either can happen when fingerprint is not
     * available or the user chooses to use the password authentication method by pressing the
     * button. This can also happen when the user had too many fingerprint attempts.
     */
    private fun goToBackup(dialog: MaterialDialog?) {
        mStage = Stage.PASSWORD
        updateStage(dialog)
        mPassword!!.requestFocus()
        // Show the keyboard.
        mPassword!!.postDelayed(mShowKeyboardRunnable, 500)
        // Fingerprint is not used anymore. Stop listening for it.
        if (mFingerprintLib != null) mFingerprintLib!!.stopListening()
    }

    private fun toggleButtonsEnabled(enabled: Boolean) {
        val dialog = dialog as MaterialDialog?
        dialog!!.getActionButton(DialogAction.POSITIVE).isEnabled = enabled
        dialog.getActionButton(DialogAction.NEGATIVE).isEnabled = enabled
    }

    private fun verifyPassword() {
        toggleButtonsEnabled(false)
        mCallback!!.onFingerprintDialogVerifyPassword(this, mPassword!!.text.toString())
    }

    @SuppressLint("NewApi")
    fun notifyPasswordValidation(valid: Boolean) {
        val dialog = dialog as MaterialDialog?
        val positive: View = dialog!!.getActionButton(DialogAction.POSITIVE)
        val negative: View = dialog.getActionButton(DialogAction.NEGATIVE)
        toggleButtonsEnabled(true)
        if (valid) {
            if (mStage == Stage.NEW_FINGERPRINT_ENROLLED &&
                mUseFingerprintFutureCheckBox!!.isChecked
            ) {
                // Re-create the key so that fingerprints including new ones are validated.
                FingerprintLib.get().recreateKey()
                mStage = Stage.FINGERPRINT
            }
            mPassword!!.setText("")
            mCallback!!.onFingerprintDialogAuthenticated()
            dismiss()
        } else {
            mPasswordDescriptionTextView!!.setText(R.string.password_not_recognized)
            val red = ContextCompat.getColor(requireActivity(), R.color.material_red_500)
            MDTintHelper.setTint(mPassword!!, red)
            (positive as TextView).setTextColor(red)
            (negative as TextView).setTextColor(red)
        }
    }

    private val mShowKeyboardRunnable = Runnable {
        if (mFingerprintLib != null) mFingerprintLib!!.mInputMethodManager.showSoftInput(
            mPassword,
            0
        )
    }

    private fun updateStage(dialog: MaterialDialog?) {
        var dialog = dialog
        if (mLastStage == null || mLastStage != mStage && mCallback != null) {
            mLastStage = mStage
            mCallback!!.onFingerprintDialogStageUpdated(this, mStage)
        }
        if (dialog == null) dialog = getDialog() as MaterialDialog?
        if (dialog == null) return
        when (mStage) {
            Stage.FINGERPRINT -> {
                dialog.setActionButton(DialogAction.POSITIVE, android.R.string.cancel)
                dialog.setActionButton(DialogAction.NEGATIVE, R.string.use_password)
                mFingerprintContent!!.visibility = View.VISIBLE
                mBackupContent!!.visibility = View.GONE
            }
            Stage.NEW_FINGERPRINT_ENROLLED, Stage.PASSWORD -> {
                dialog.setActionButton(DialogAction.POSITIVE, android.R.string.cancel)
                dialog.setActionButton(DialogAction.NEGATIVE, android.R.string.ok)
                mFingerprintContent!!.visibility = View.GONE
                mBackupContent!!.visibility = View.VISIBLE
                if (mStage == Stage.NEW_FINGERPRINT_ENROLLED) {
                    mPasswordDescriptionTextView!!.visibility = View.GONE
                    mNewFingerprintEnrolledTextView!!.visibility = View.VISIBLE
                    mUseFingerprintFutureCheckBox!!.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onEditorAction(v: TextView, actionId: Int, event: KeyEvent): Boolean {
        if (actionId == EditorInfo.IME_ACTION_GO) {
            verifyPassword()
            return true
        }
        return false
    }

    /**
     * Enumeration to indicate which authentication method the user is trying to authenticate with.
     */
    enum class Stage {
        FINGERPRINT, NEW_FINGERPRINT_ENROLLED, PASSWORD
    }

    private fun showError(error: CharSequence?) {
        if (activity == null) return
        mFingerprintIcon!!.setImageResource(R.drawable.ic_fingerprint_error)
        mFingerprintStatus!!.text = error
        mFingerprintStatus!!.setTextColor(ContextCompat.getColor(requireActivity(), R.color.warning_color))
        mFingerprintStatus!!.removeCallbacks(mResetErrorTextRunnable)
        mFingerprintStatus!!.postDelayed(mResetErrorTextRunnable, ERROR_TIMEOUT_MILLIS)
    }

    var mResetErrorTextRunnable = Runnable {
        if (activity == null) return@Runnable
        mFingerprintStatus!!.setTextColor(
            Utils.resolveColor(
                activity, android.R.attr.textColorSecondary
            )
        )
        mFingerprintStatus!!.text = resources.getString(R.string.fingerprint_hint)
        mFingerprintIcon!!.setImageResource(R.drawable.ic_fp_40px)
    }

    // FingerprintLib callbacks
    override fun onFingerprintLibReady(fingerprintLib: FingerprintLib) {
        fingerprintLib.startListening()
    }

    override fun onFingerprintLibListening(newFingerprint: Boolean) {
        mFingerprintStatus!!.setText(R.string.fingerprint_hint)
        if (newFingerprint) mStage = Stage.NEW_FINGERPRINT_ENROLLED
        updateStage(null)
    }

    override fun onFingerprintLibAuthenticated(fingerprintLib: FingerprintLib) {
        toggleButtonsEnabled(false)
        mFingerprintStatus!!.removeCallbacks(mResetErrorTextRunnable)
        mFingerprintIcon!!.setImageResource(R.drawable.ic_fingerprint_success)
        mFingerprintStatus!!.setTextColor(ContextCompat.getColor(requireActivity(), R.color.success_color))
        mFingerprintStatus!!.text = resources.getString(R.string.fingerprint_success)
        mFingerprintIcon!!.postDelayed({
            mCallback!!.onFingerprintDialogAuthenticated()
            dismiss()
        }, SUCCESS_DELAY_MILLIS)
    }

    override fun onFingerprintLibError(
        fingerprintLib: FingerprintLib,
        type: FingerprintLibErrorType,
        e: Exception
    ) {
        when (type) {
            FingerprintLibErrorType.FINGERPRINTS_UNSUPPORTED -> goToBackup(null)
            FingerprintLibErrorType.UNRECOVERABLE_ERROR, FingerprintLibErrorType.PERMISSION_DENIED -> {
                showError(e.message)
                mFingerprintIcon!!.postDelayed({ goToBackup(null) }, ERROR_TIMEOUT_MILLIS)
            }
            FingerprintLibErrorType.REGISTRATION_NEEDED -> {
                mPasswordDescriptionTextView!!.setText(R.string.no_fingerprints_registered)
                goToBackup(null)
            }
            FingerprintLibErrorType.HELP_ERROR -> showError(e.message)
            FingerprintLibErrorType.FINGERPRINT_NOT_RECOGNIZED -> showError(resources.getString(R.string.fingerprint_not_recognized))
        }
    }

    companion object {
        const val ERROR_TIMEOUT_MILLIS: Long = 1600
        const val SUCCESS_DELAY_MILLIS: Long = 1300
        const val TAG = "[FPLIB_FPDIALOG]"
        fun <T> show(
            context: T,
            keyName: String?,
            requestCode: Int
        ): FingerprintDialog where T : FragmentActivity?, T : Callback? {
            return show(context, keyName, requestCode, true)
        }

        fun <T> show(
            context: T,
            keyName: String?,
            requestCode: Int,
            cancelable: Boolean
        ): FingerprintDialog where T : FragmentActivity?, T : Callback? {
            var dialog = getVisible(context)
            dialog?.dismiss()
            dialog = FingerprintDialog()
            val args = Bundle()
            args.putString("key_name", keyName)
            args.putInt("request_code", requestCode)
            args.putBoolean(
                "was_initialized",
                FingerprintLib.get() != null && FingerprintLib.get().mCallback === context
            )
            args.putBoolean("cancelable", cancelable)
            dialog.arguments = args
            dialog.show(context!!.supportFragmentManager, TAG)
            return dialog
        }

        fun <T : FragmentActivity?> getVisible(context: T): FingerprintDialog? {
            val frag = context!!.supportFragmentManager.findFragmentByTag(TAG)
            return if (frag != null && frag is FingerprintDialog) frag else null
        }
    }
}