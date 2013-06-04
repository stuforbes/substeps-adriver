Feature: A Feature to test the various form related built in steps of substeps-adriver

Background:
	Given I am on the form-tests page
	Then I can see the page titled "Form tests"


Scenario: Text tests
	When I find a field with id text-field-1 and type "This is text field 1"
	Then the content area will say "text-field-1 says: This is text field 1"
	
	When I find a field with id text-field-1 and type " with some more text"
	Then the content area will say "text-field-1 says: This is text field 1 with some more text"
	
	When I find a field with id text-field-1 and set the text to "Some new text"
	Then the content area will say "text-field-1 says: Some new text"
	
	
Scenario: SelectBox tests
	When I find a field with id select-box-1 and select the option "Option 1"
	Then the content area will say "select-box-1 says: option-1 is selected"
	Then the option "Option 1" will be selected in select-box-1
	Then the option "Option 3" will not be selected in select-box-1	
	

Scenario: Checkbox tests
	When I check the checkbox checkbox-field-1
	Then the content area will say "checkbox-field-1 says: Is now checked"
	Then the checkbox checkbox-field-1 will be checked
	
	When I uncheck the checkbox checkbox-field-1
	Then the content area will say "checkbox-field-1 says: Is not checked"
	Then the checkbox checkbox-field-1 will not be checked
	
Scenario: Radio tests
	When I check the radio radio-1
	Then the content area will say "radio-1 says: Is now checked"
	Then the radio radio-1 will be checked
	
	When I check the radio radio-2
	Then the content area will say "radio-2 says: Is now checked"
	Then the radio radio-1 will not be checked	