import random

# Get user's name
user = input("What is your name? ")

# List of possible choices
choices = ["rock", "paper", "scissors"]

# Get user's choice
user_choice = input(user + ", choose rock, paper or scissors: ").lower()

# Computer randomly chooses
computer_choice = random.choice(choices)
print("Computer chose:", computer_choice)

# Determine the winner
if user_choice == computer_choice:
    print("It's a tie!")
elif user_choice == "rock":
    if computer_choice == "scissors":
        print("Rock wins! You win!")
    else:
        print("Paper wins! Computer wins!")
elif user_choice == "scissors":
    if computer_choice == "paper":
        print("Scissors win! You win!")
    else:
        print("Rock wins! Computer wins!")
elif user_choice == "paper":
    if computer_choice == "rock":
        print("Paper wins! You win!")
    else:
        print("Scissors win! Computer wins!")
else:
    print("Invalid input! Please choose rock, paper or scissors.")