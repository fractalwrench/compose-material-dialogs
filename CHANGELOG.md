# Changelog

### 0.3.1 - 2021-03-10

  - Update compose version to 1.0.0-beta02
  - Fix input component bug and re-add it to the core module

### 0.3.0 - 2021-03-01

  - Update compose version to 1.0.0-beta01
  - Redesign date picker and time picker to match the material specification ([material.io]())
    - Introduce year picker
    - Add `colors` attributes to the time picker dialog to allow for more customisation (see time picker documentation)
    - Add am/pm toggle instead of using 24 hour time 
    - Known Issue: calendar view can stutter when swiping between months
  - Temporarily remove `input` dialog component due to an unknown internal bug. This will be re-added as soon as a fix/workaround can be found and `customView` can be used in the meantime.
  - Add `button` function to the `MaterialDialogButtons` class which should be used when the button action is neutral. This button will be displayed in-between any `positive` and `negative` buttons which are being used

### 0.2.11 - 2021-01-14

  - Fix issues with disabling positive button for the `input` and `listItemsSingleChoice` components
  - Fix bug lifecycle bug for repeated callbacks on subsequent uses of the same dialog
  - Add `keyboardOptions` parameter to `input` component
  - Remove `allowEmpty` parameter from `input` component in favour of using `isTextValid` parameter to do this validation
  - Add `disablePositiveButton` and `enablePositiveButton` methods to the `MaterialDialog` class to allow for further customisation of the dialog when using custom layouts

### 0.2.10  - 2021-01-13

  - Updated compose version to 1.0.0-alpha10
  - Add `onCloseRequest` parameter to the `MaterialDialog` constructor

### 0.2.9  - 2021-01-08

  - Update usages of remember to onCommit
  - Fix concurrency issues during button creation

### 0.2.8  - 2020-12-17

  - Updated compose version to 1.0.0-alpha09
  - Fix bug in input dialog component which caused callback to be called multiple times
  - Add documentation and code sample for input dialog component

### 0.2.7  - 2020-12-05

  - Updated compose version to 1.0.0-alpha08

### 0.2.6  - 2020-11-11

  - Updated compose version to 1.0.0-alpha07
  - Fix core module manifest to fix errors

### 0.2.5  - 2020-10-28

  - Updated compose version to 1.0.0-alpha06

### 0.2.4  - 2020-10-15

  - Updated compose version to 1.0.0-alpha05
  - Fixed crash when dialog button values such as text are changed
  - Clean up gradle files

### 0.2.3  - 2020-10-02

  - Updated compose version to 1.0.0-alpha04
  - Add optimisations to date picker 
  - Fix ARGB color picker layout

### 0.2.2  - 2020-09-16

  - Updated compose version to 1.0.0-alpha03

### 0.2.1  - 2020-09-02

  - Updated compose version to 1.0.0-alpha02

### 0.2.0  - 2020-08-26

  - Updated compose version to 1.0.0-alpha01

### 0.1.8  - 2020-08-19

  - Updated compose version to dev17
  - Updated kotlin version to 1.4.0
  - Fix date dialog and time dialog to also fit on smaller screens

### 0.1.6  - 2020-08-05

  - Updated compose version to dev16
  - Updated kotlin version to 1.4.0-rc
  - Fix datetime dialog to fit on smaller screens

### 0.1.5  - 2020-07-23

  - Updated compose version to dev15
  - Updated kotlin version to 1.4-M3
  - Make dialog button text uppercase

