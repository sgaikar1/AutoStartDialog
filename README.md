# AutoStartDialog


The goal of this library is to allow your app to support on chinese rom's which will allow your app to work in background to recive Push Notifications

## Preview

![gif](https://github.com/sgaikar1/AutoStartDialog/blob/master/screenshot.gif)



### Installation

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

dependencies {
    implementation 'com.github.sgaikar1:AutoStartDialog:1.0.1'
}
```

 ## Attributes
 
 | Name        | Description           | Value  |
 | ------------- |:-------------:| -----:|
 | openSettingsWithoutShowingDialog    | To open settings without showing dialog     | boolean|
 | setTitle|Title for dialog     |   String |
 | setTitleTextColor|color for title     |   int color resource |
 | setMessage | Descriptive message for dialog      |   String|
 | setButtonTextColor|color for buttons     |   int color resource |
 | setPositiveBtnText | Positive button text     |   String|
 | setNegativeBtnText | Negative button text     |   String|
 | setCancelableOnTouchOutside | set if dialog is dismissable on outside touch   |   Boolean|
 | onPositiveClicked/ onNegativeClicked|ClickListeners for buttons  | AutostartDialogListener  |

### Usage

* For implementation please refer to the sample app.
```
  AutostartDialog.Builder(this)
            .openSettingsWithoutShowingDialog(true)
            .setTitle("Allow AutoStart")
            .setMessage("Please enable auto start in settings.")
            .setPositiveBtnText("Allow")
            .setNegativeBtnText("No")
            .setCancelableOnTouchOutside(false)
            .onPositiveClicked(object: AutostartDialogListener {
                override fun onClick(input: String) {
                  Toast.makeText(this@MainActivity,input,Toast.LENGTH_SHORT).show()
                }
            })
            .onNegativeClicked(object: AutostartDialogListener {
                override fun onClick(input: String) {
                    Toast.makeText(this@MainActivity,"Negative clicked",Toast.LENGTH_SHORT).show()
                }
            })
            .setTitleTextColor(ContextCompat.getColor(this, android.R.color.black))
            .setMessageTextColor(ContextCompat.getColor(this, android.R.color.black))
            .setButtonTextColor(ContextCompat.getColor(this, android.R.color.black))
            .build()
            
```

## Contribute
We can collaboratively make it happen. So if you have any issues, new ideas about implementations then just raise issue and we are open for Pull Requests. Improve and make it happen.
See [Contributing Guidelines](Contributing.md).

## Credits

 * To StackOverFlow Community: This project is made by implementing suggestions/answers posted by android dev community on stackoverflow,
  If you find your code is used in this library feel free to ping me on mail, i will give proper credits in the repository.
 
 
 ## License

* [Apache Version 2.0](http://www.apache.org/licenses/LICENSE-2.0.html)

```
Copyright 2020 Santosh Gaikar....

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
