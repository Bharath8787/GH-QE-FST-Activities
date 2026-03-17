@activity6
Feature: To test input with Datatables

Scenario: Adding items to a to-do list
	Given user is on the To-Do list page
	When user adds the following tasks
		| task1 |
		| task2 |
		| task3 |
	Then they can see the task added to the list