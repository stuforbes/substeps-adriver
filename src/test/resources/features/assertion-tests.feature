Tags: wip
Feature: A Feature to test the various finder related built in steps of substeps-adriver

Background:
	Given I am on the finder-and-assertion-tests page
	Then I can see the page titled "Finder and Assertion tests"

Scenario: Assert element with id has text
	Then the element with id "content" will have the text "This is some content"
	
Scenario: Assert child element with xpath has text
	When I search for an element by id "list"
	Then a child found by "//li" will have the text "List item 2"
	
Scenario: Assert current element has text
	When I search for an element by id "a-link"
	Then the current element will have the text "Link text"
	
Scenario: Assert current element contains text
	When I search for an element by id "a-link"
	Then the current element will contain the text "Link t"
	
Scenario: Assert current element contains an attribute
	When I search for an element by xpath "//div/a"
	Then the current element will have a id of "a-link"

Scenario: Assert there is an element with a tag that contains text
	Then there is an element with tag "div" that contains the text "This is some content"
	
Scenario: Assert there is an element with a tag that contains an attribute
	Then there is an element with tag "span" that has a class of "a-span"
	
Scenario: Assert the page source can contain content
	Then the page source contains the string "<form>"
	
Scenario: Assert the page source does not contain content
	Then the page source does not contain the string "Some text that does not live on this page"
	
Scenario: Assert that an element isnt empty
	Then the element with id "content" is not empty
	
	

Scenario: Find child by name test
	When I search for an element by xpath "//form"
	When I search for a child of the current item with the name "checkbox"
	Then the current element will have a value of "a-checkbox"