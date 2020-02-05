package com.sgaikar1.autostartdialog

import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat

class AutostartDialog {

    class Builder(private val context: Context) {
        private var title: String? = null
        private var openSettingsDirectly: Boolean = false
        private var message: String? = null
        private var positiveBtnText: String? = null
        private var negativeBtnText: String? = null
        private var buttonTextColor: Int = 0
        private var titleTextColor: Int = 0
        private var messageTextColor: Int = 0
        private var fontId: Int = 0
        private var cancelOnTouchOutside = true
        private var positiveListener: AutostartDialogListener? = null
        private var negativeListener: AutostartDialogListener? = null

        private val isAutoStartBrand: Boolean
            get() {
                return when (Build.BRAND.toLowerCase()) {
                    "xiaomi", "letv", "honor", "oppo", "vivo", "asus" -> true
                    else -> false
                }
            }

        fun setCancelableOnTouchOutside(cancelOnTouchOutside: Boolean): Builder {
            this.cancelOnTouchOutside = cancelOnTouchOutside
            return this
        }

        fun setCustomFont(fontId: Int): Builder {
            this.fontId = fontId
            return this
        }

        fun setTitle(title: String): Builder {
            this.title = title
            return this
        }


        fun openSettingsWithoutShowingDialog(openSettingsDirectly: Boolean): Builder {
            this.openSettingsDirectly= openSettingsDirectly
            return this
        }

        fun setMessage(message: String): Builder {
            this.message = message
            return this
        }

        fun setTitleTextColor(titleTextColor: Int): Builder {
            this.titleTextColor = titleTextColor
            return this
        }

        fun setMessageTextColor(messageTextColor: Int): Builder {
            this.messageTextColor = messageTextColor
            return this
        }


        fun setButtonTextColor(buttonTextColor: Int): Builder {
            this.buttonTextColor = buttonTextColor
            return this
        }

        fun setPositiveBtnText(positiveBtnText: String): Builder {
            this.positiveBtnText = positiveBtnText
            return this
        }

        fun setNegativeBtnText(negativeBtnText: String): Builder {
            this.negativeBtnText = negativeBtnText
            return this
        }

        fun onPositiveClicked(positiveListener: AutostartDialogListener): Builder {
            this.positiveListener = positiveListener
            return this
        }

        fun onNegativeClicked(negativeListener: AutostartDialogListener): Builder {
            this.negativeListener = negativeListener
            return this
        }

        fun build() {
            val style = R.style.AutostartDialogTheme

            val dialog = AlertDialog.Builder(context).create()

            dialog.setCanceledOnTouchOutside(cancelOnTouchOutside)

            @SuppressLint("InflateParams")
            val v = LayoutInflater.from(context).inflate(R.layout.autostart_dialog_layout, null)
            dialog.setView(v)

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            }

            dialog.window!!.requestFeature(Window.FEATURE_NO_TITLE)

            val tvTitle = v.findViewById<TextView>(R.id.tv_autostart_dialog_layout_title)
            val tvMessage = v.findViewById<TextView>(R.id.tv_autostart_dialog_layout_message)

            val btnNegativeText =
                v.findViewById<TextView>(R.id.tv_autostart_dialog_layout_cancel_text)
            val btnPositiveText =
                v.findViewById<TextView>(R.id.tv_autostart_dialog_layout_confirm_text)

            val btnNegative = v.findViewById<View>(R.id.v_autostart_dialog_layout_cancel)
            val btnPositive = v.findViewById<View>(R.id.v_autostart_dialog_layout_confirm)

            val buttonDivider = v.findViewById<View>(R.id.v_autostart_dialog_layout_button_divider)

            if (fontId != 0) {
                val tf = ResourcesCompat.getFont(context, fontId)
                tvTitle.typeface = tf
                tvMessage.typeface = tf
                btnPositiveText.typeface = tf
                btnNegativeText.typeface = tf
            }

            if (title != null)
                tvTitle.text = title
            else
                tvTitle.visibility = View.GONE

            if (message != null)
                tvMessage.text = message
            else
                tvMessage.visibility = View.GONE

            if (titleTextColor != 0) {
                tvTitle.setTextColor(titleTextColor)
            }

            if (messageTextColor != 0) {
                tvMessage.setTextColor(messageTextColor)
            }

            if (buttonTextColor != 0) {
                btnPositiveText.setTextColor(buttonTextColor)
                btnNegativeText.setTextColor(buttonTextColor)
            }

            if (positiveBtnText != null) {
                btnPositiveText.text = positiveBtnText
            }

            if (negativeBtnText != null) {
                btnNegativeText.text = negativeBtnText
            }

            if (positiveListener != null) {
                btnPositive.visibility = View.VISIBLE
                btnPositive.setOnClickListener {
                    openAutoStartSetting(context)
                    positiveListener?.onClick("Positive clicked")
                    dialog.dismiss()
                }

            } else {
                btnPositive.visibility = View.GONE
            }

            if (negativeListener != null) {
                btnNegative.visibility = View.VISIBLE
                btnNegative.setOnClickListener { dialog.dismiss() }
            } else {
                btnNegative.visibility = View.GONE
                val params = buttonDivider.layoutParams as ConstraintLayout.LayoutParams
                params.horizontalBias = 0f
                buttonDivider.layoutParams = params
            }

            if (isAutoStartBrand) {
                if(openSettingsDirectly){
                    openAutoStartSetting(context)
                }else {
                    dialog.show()
                }
            }
        }

        private fun openAutoStartSetting(context: Context) {
            for (intentt in getListOfIntents(context)) {
                if (isCallable(context, intentt)) {
                    if (context.packageManager.resolveActivity(
                            intentt,
                            PackageManager.MATCH_DEFAULT_ONLY
                        ) != null
                    ) {
                        try {
                            context.startActivity(intentt)
                        } catch (ex: SecurityException) {
                            checkManufacturerAndStartActivity(context)
                        }

                    }
                }
            }
        }

        private fun getListOfIntents(context: Context): Iterable<Intent> {
            return listOf(
                Intent().setComponent(
                    ComponentName(
                        "com.miui.securitycenter",
                        "com.miui" + ".permcenter.autostart.AutoStartManagementActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.letv.android.letvsafe",
                        "com.letv" + ".android.letvsafe.AutobootManageActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei" + ".systemmanager.optimize.process.ProtectActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.huawei.systemmanager",
                        "com.huawei" + ".systemmanager.appcontrol.activity.StartupAppControlActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros" + ".safecenter.permission.startup.StartupAppListActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.coloros.safecenter",
                        "com.coloros" + ".safecenter.startupapp.StartupAppListActivity"
                    )
                ).setData(
                    Uri.fromParts(
                        "package", context.packageName, null
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.oppo.safe",
                        "com.oppo.safe" + ".permission.startup.StartupAppListActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.iqoo.secure",
                        "com.iqoo.secure.ui" + ".phoneoptimize.AddWhiteListActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.iqoo.secure",
                        "com.iqoo.secure.ui" + ".phoneoptimize.BgStartUpManager"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.vivo.permissionmanager",
                        "com.vivo" + ".permissionmanager.activity.BgStartUpManagerActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.asus.mobilemanager",
                        "com.asus" + ".mobilemanager.entry.FunctionActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.asus.mobilemanager",
                        "com.asus" + ".mobilemanager.autostart.AutoStartActivity"
                    )
                ),
                Intent().setComponent(
                    ComponentName(
                        "com.letv.android.letvsafe",
                        "com.letv" + ".android.letvsafe.AutobootManageActivity"
                    )
                )
                    .setData(Uri.parse("mobilemanager://function/entry/AutoStart")),
                Intent().setComponent(
                    ComponentName(
                        "com.meizu.safe",
                        "com.meizu.safe" + ".security.SHOW_APPSEC"
                    )
                ).addCategory(Intent.CATEGORY_DEFAULT).putExtra(
                    "packageName", BuildConfig.APPLICATION_ID
                )
            )
        }

        private fun checkManufacturerAndStartActivity(context: Context) {
            when (Build.MANUFACTURER.toLowerCase()) {
                "oppo" -> getOppDeviceSpecificIntents(context)
                "xiaomi" -> context.startActivity(
                    Intent().setComponent(
                        ComponentName(
                            "com.miui" + ".securitycenter",
                            "com.miui.permcenter.autostart.AutoStartManagementActivity"
                        )
                    )
                )
                "asus" -> context.startActivity(
                    Intent().setClassName(
                        "com.asus.mobilemanager",
                        "com" + ".asus.mobilemanager.autostart.AutoStartActivity"
                    )
                )
                else -> {
                }
            }
        }

        private fun getOppDeviceSpecificIntents(context: Context) {
            try {
                context.startActivity(
                    Intent().setComponent(
                        ComponentName(
                            "com.coloros" + ".safecenter",
                            "com.coloros.safecenter.permission.startup" + ".StartupAppListActivity"
                        )
                    )
                )
            } catch (e: Exception) {
                try {
                    context.startActivity(
                        Intent().setComponent(
                            ComponentName(
                                "com.coloros" + ".safecenter",
                                "com.coloros.safecenter.permission.startupapp" + ".StartupAppListActivity"
                            )
                        )
                    )
                } catch (e1: Exception) {
                    try {
                        context.startActivity(
                            Intent().setComponent(
                                ComponentName(
                                    "com" + ".coloros.safecenter",
                                    "com.coloros.safecenter.permission" + ".startupmanager.StartupAppListActivity"
                                )
                            )
                        )
                    } catch (e2: Exception) {
                        try {
                            context.startActivity(
                                Intent().setComponent(
                                    ComponentName(
                                        "com" + ".coloros.safe",
                                        "com.coloros.safe.permission.startup" + ".StartupAppListActivity"
                                    )
                                )
                            )
                        } catch (e3: Exception) {
                            try {
                                context.startActivity(
                                    Intent().setComponent(
                                        ComponentName(
                                            "com.coloros.safe",
                                            "com.coloros.safe.permission.startupapp" + ".StartupAppListActivity"
                                        )
                                    )
                                )
                            } catch (e4: Exception) {
                                try {
                                    context.startActivity(
                                        Intent().setComponent(
                                            ComponentName(
                                                "com.coloros.safe",
                                                "com.coloros.safe.permission.startupmanager.StartupAppListActivity"
                                            )
                                        )
                                    )
                                } catch (e5: Exception) {
                                    try {
                                        context.startActivity(
                                            Intent().setComponent(
                                                ComponentName(
                                                    "com.coloros.safecenter",
                                                    "com.coloros.safecenter.permission.startsettings"
                                                )
                                            )
                                        )
                                    } catch (e6: Exception) {
                                        try {
                                            context.startActivity(
                                                Intent().setComponent(
                                                    ComponentName(
                                                        "com.coloros.safecenter",
                                                        "com.coloros.safecenter.permission.startupapp.startupmanager"
                                                    )
                                                )
                                            )
                                        } catch (e7: Exception) {
                                            try {
                                                context.startActivity(
                                                    Intent().setComponent(
                                                        ComponentName(
                                                            "com.coloros.safecenter",
                                                            "com.coloros.safecenter.permission.startupmanager.startupActivity"
                                                        )
                                                    )
                                                )
                                            } catch (e8: Exception) {
                                                try {
                                                    context.startActivity(
                                                        Intent().setComponent(
                                                            ComponentName(
                                                                "com.coloros.safecenter",
                                                                "com.coloros.safecenter.permission.startup.startupapp.startupmanager"
                                                            )
                                                        )
                                                    )
                                                } catch (e9: Exception) {
                                                    try {
                                                        context.startActivity(
                                                            Intent().setComponent(
                                                                ComponentName(
                                                                    "com.coloros.safecenter",
                                                                    "com.coloros.privacypermissionsentry.PermissionTopActivity.Startupmanager"
                                                                )
                                                            )
                                                        )
                                                    } catch (e10: Exception) {
                                                        try {
                                                            context.startActivity(
                                                                Intent().setComponent(
                                                                    ComponentName(
                                                                        "com.coloros.safecenter",
                                                                        "com.coloros.privacypermissionsentry.PermissionTopActivity"
                                                                    )
                                                                )
                                                            )
                                                        } catch (e11: Exception) {
                                                            try {
                                                                context.startActivity(
                                                                    Intent().setComponent(
                                                                        ComponentName(
                                                                            "com.coloros.safecenter",
                                                                            "com.coloros.safecenter.FakeActivity"
                                                                        )
                                                                    )
                                                                )
                                                            } catch (e12: Exception) {
                                                                try {
                                                                    Intent().component =
                                                                        ComponentName(
                                                                            "com.oppo.safe",
                                                                            "com.oppo.safe.permission.startup.StartupAppListActivity"
                                                                        )
                                                                } catch (e13: Exception) {
                                                                    e13.printStackTrace()
                                                                }

                                                            }

                                                        }

                                                    }

                                                }

                                            }

                                        }

                                    }

                                }

                            }

                        }

                    }

                }

            }

        }


        private fun isCallable(context: Context, intent: Intent): Boolean {
            val list = context.packageManager.queryIntentActivities(
                intent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            return list.size > 0
        }

    }


}