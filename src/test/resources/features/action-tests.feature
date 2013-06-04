Feature: A Feature to test the various action related built in steps of substeps-adriver

Background:
	Given I am on the action-tests page
	Then I can see the page titled "Action tests"

Scenario: Click by id test
	When I click on the element with id "link"
	Then the content area will say "The link has been clicked"
	
	When I click on the element with id "panel"
	Then the content area will say "The panel has been left clicked"
	
Scenario: Find and click test
	When I find the link first by id "link" and then click it
	Then the content area will say "The link has been clicked"
	
Scenario: Click button test
	When I click on the button "Button"
	Then the content area will say "The button has been clicked"
	
Scenario: Double click test
	When I double click on the element with id "panel"
	Then the content area will say "The panel has been double clicked"
	
Scenario: Right click test
	When I right click on the element with id "panel"
	Then the content area will say "The panel has been right clicked"	