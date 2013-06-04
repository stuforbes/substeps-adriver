Feature: A Feature to test the various finder related built in steps of substeps-adriver

Background:
	Given I am on the finder-and-assertion-tests page
	Then I can see the page titled "Finder and Assertion tests"

Scenario: Find by id test
	When I search for an element by id "content"
	Then the current element will contain the text "This is some content"
	
Scenario: Find by xpath test
	When I search for an element by xpath "//div/form/input[@type='checkbox']"
	Then the current element will have a value of "a-checkbox"
	
Scenario: Find by name test
	When I search for an element by name "textfield"
	Then the current element will have a value of "Some text"
	
Scenario: Find by tag and attribute test
	When I search for an element with a tag name of "li" and a class of "active"
	Then the current element will contain the text "List item 1"

Scenario: Find by tag and text text
	When I search for an element with a tag name of "li" and inner text of "List item 3"
	Then the current element will contain the text "List item 3"

Scenario: Find child by name test
	When I search for an element by xpath "//form"
	When I search for a child of the current item with the name "checkbox"
	Then the current element will have a value of "a-checkbox"