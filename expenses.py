"""Command line expense tracker (data stored in a CSV file).

The file emma_expenses.csv shipped with the project already contains a full
year of expenses, so the statistics are meaningful from the first run.
"""

import csv
import math
import os
from datetime import datetime

FILENAME = "emma_expenses.csv"  # or "personnal_expenses.csv"
COLUMNS = ["date", "category", "description", "amount"]
HOUSING_CATEGORY = "housing"
# Banks usually refuse a loan when the monthly payment goes over 35% of the income.
MAX_DEBT_RATIO = 0.35


def read_expenses():
    """Return the list of expenses saved in the CSV file."""
    if not os.path.exists(FILENAME):
        return []

    with open(FILENAME, "r", newline="", encoding="utf-8") as file:
        reader = csv.DictReader(file)
        return list(reader)


def totals_by_category(expenses):
    """Return a dictionary {category: total spent} built from the expenses."""
    totals = {}

    for expense in expenses:
        category = expense["category"]
        amount = float(expense["amount"])

        # The first time we meet a category, its total starts at 0.
        if category not in totals:
            totals[category] = 0

        totals[category] += amount

    return totals


def count_months(expenses):
    """Return how many different months are covered by the expenses."""
    months = set()

    for expense in expenses:
        # A date looks like "05/03/2025": we only keep "03/2025".
        day, month, year = expense["date"].split("/")
        months.add(f"{month}/{year}")

    return len(months)


def total_amount(expenses):
    """Return the sum of every expense."""
    total = 0

    for expense in expenses:
        total += float(expense["amount"])

    return total


def average_monthly_spending(expenses):
    """Return the average amount spent per month over the recorded period."""
    total = total_amount(expenses)
    months = count_months(expenses)

    return total / months


def average_monthly_housing(expenses):
    """Return the average amount spent per month on housing (rent)."""
    total = 0

    for expense in expenses:
        if expense["category"] == HOUSING_CATEGORY:
            total += float(expense["amount"])

    months = count_months(expenses)

    return total / months


def ask_number(question):
    """Ask a question and return the answer as a number, or None if invalid."""
    answer = input(question).strip()

    # In French a comma is used for decimals, but Python needs a dot.
    answer = answer.replace(",", ".")

    try:
        return float(answer)
    except ValueError:
        return None


def show_history():
    """Display every saved expense along with the total."""
    expenses = read_expenses()

    if not expenses:
        print("No expense saved yet.\n")
        return

    print("\n--- Expense history ---")
    header = f"{'Date':<12}{'Category':<20}{'Description':<30}{'Amount':>10}"
    print(header)

    total = 0
    for expense in expenses:
        date = expense["date"]
        category = expense["category"]
        description = expense["description"]
        amount = float(expense["amount"])
        total += amount

        line = f"{date:<12}{category:<20}{description:<30}{amount:>10.2f}"
        print(line)

    print(f"\nTotal: {total:.2f} EUR over {len(expenses)} expense(s).\n")


def add_expense():
    """Ask for the details of an expense and append it to the CSV file."""
    category = input("Category (e.g. groceries, transport): ").strip()
    description = input("Description: ").strip()
    amount = ask_number("Amount in euros: ")

    if amount is None:
        print("Invalid amount, the expense was not saved.\n")
        return

    file_exists = os.path.exists(FILENAME)
    today = datetime.now().strftime("%d/%m/%Y")
    new_row = [today, category, description, f"{amount:.2f}"]

    with open(FILENAME, "a", newline="", encoding="utf-8") as file:
        writer = csv.writer(file)

        # A brand new file needs its header line first.
        if not file_exists:
            writer.writerow(COLUMNS)

        writer.writerow(new_row)

    print(f"Expense of {amount:.2f} EUR saved.\n")


def show_statistics():
    """Display the total and the monthly budget of each category."""
    expenses = read_expenses()

    if not expenses:
        print("No expense saved yet.\n")
        return

    totals = totals_by_category(expenses)
    grand_total = total_amount(expenses)
    months = count_months(expenses)

    # We sort the categories on their total (totals.get gives it),
    # and reverse=True shows the biggest one first.
    sorted_categories = sorted(totals, key=totals.get, reverse=True)

    print("\n--- Statistics by category ---")
    header = f"{'Category':<20}{'Total':>12}{'Per month':>12}{'Share':>10}"
    print(header)

    for category in sorted_categories:
        total = totals[category]
        monthly = total / months
        share = total / grand_total * 100

        line = f"{category:<20}{total:>12.2f}{monthly:>12.2f}{share:>9.1f}%"
        print(line)

    monthly_budget = grand_total / months
    average = grand_total / len(expenses)

    print(f"\nTotal: {grand_total:.2f} EUR over {len(expenses)} expense(s) and {months} month(s).")
    print(f"Average monthly budget: {monthly_budget:.2f} EUR.")
    print(f"Average per expense: {average:.2f} EUR.\n")


def plan_car_purchase():
    """Compute how much can be saved each month to buy a car, and when."""
    expenses = read_expenses()

    if not expenses:
        print("No expense saved yet, the monthly budget cannot be computed.\n")
        return

    income = ask_number("Monthly net income in euros: ")
    price = ask_number("Price of the car in euros: ")

    if income is None or price is None:
        print("Invalid amount, the plan could not be computed.\n")
        return

    spending = average_monthly_spending(expenses)
    savings = income - spending

    print("\n--- Car purchase plan ---")
    print(f"Average monthly spending: {spending:>10.2f} EUR")
    print(f"Monthly net income:       {income:>10.2f} EUR")
    print(f"Available savings:        {savings:>10.2f} EUR per month")

    if savings <= 0:
        print("\nYour expenses are higher than your income, saving is not possible yet.\n")
        return

    # math.ceil rounds up: a partial month is not enough to pay for the car.
    months = math.ceil(price / savings)
    years = months / 12

    print(f"\nTo buy a car at {price:.2f} EUR, you need to save for {months} month(s),")


def borrowing_capacity(payment, rate, years):
    """Return the amount that can be borrowed with a given monthly payment.

    This is the classic amortized loan formula:
    capital = payment * (1 - (1 + monthly_rate) ** -months) / monthly_rate
    """
    months = years * 12
    monthly_rate = rate / 100 / 12

    # Without interest the capital is simply every payment added up.
    if monthly_rate == 0:
        return payment * months

    # What one euro paid at the end of the loan is worth today.
    discount = 1 / (1 + monthly_rate) ** months
    capital = payment * (1 - discount) / monthly_rate

    return capital


def plan_home_loan():
    """Compute how much can be borrowed to buy a home."""
    expenses = read_expenses()

    if not expenses:
        print("No expense saved yet, the monthly budget cannot be computed.\n")
        return

    income = ask_number("Monthly net income in euros: ")
    rate = ask_number("Yearly interest rate in percent (e.g. 3.5): ")
    length = ask_number("Length of the loan in years (e.g. 20): ")

    if income is None or rate is None or length is None:
        print("Invalid value, the plan could not be computed.\n")
        return

    # A loan is counted in whole years.
    years = int(length)

    spending = average_monthly_spending(expenses)
    savings = income - spending
    housing = average_monthly_housing(expenses)

    # Once the home is bought there is no rent to pay any more, so the rent
    # can go into the loan payment on top of the money already saved.
    payment = savings + housing
    max_payment = income * MAX_DEBT_RATIO

    print("\n--- Home loan plan ---")
    print(f"Current housing cost:     {housing:>10.2f} EUR per month")
    print(f"Available savings:        {savings:>10.2f} EUR per month")
    print(f"Possible loan payment:    {payment:>10.2f} EUR per month")

    if payment <= 0:
        print("\nYour expenses are higher than your income, no loan can be planned yet.\n")
        return

    if payment > max_payment:
        payment = max_payment
        max_ratio = MAX_DEBT_RATIO * 100
        print(f"Capped at {max_ratio:.0f}% of the income: {payment:.2f} EUR per month")

    capacity = borrowing_capacity(payment, rate, years)
    total_paid = payment * years * 12
    credit_cost = total_paid - capacity

    print(f"\nOver {years} year(s) at {rate:.2f}%, you can borrow {capacity:.2f} EUR.")
    print(f"Total paid to the bank: {total_paid:.2f} EUR")
    print(f"Cost of the credit:     {credit_cost:.2f} EUR\n")


def show_menu():
    """Display the available choices."""
    print("=== Expense tracker ===")
    print("1. Show history")
    print("2. Add an expense")
    print("3. Show statistics")
    print("4. Plan a car purchase")
    print("5. Plan a home loan")
    print("6. Quit")


def main():
    while True:
        show_menu()
        choice = input("Your choice: ").strip()

        if choice == "1":
            show_history()
        elif choice == "2":
            add_expense()
        elif choice == "3":
            show_statistics()
        elif choice == "4":
            plan_car_purchase()
        elif choice == "5":
            plan_home_loan()
        elif choice == "6":
            print("See you soon!")
            break
        else:
            print("Invalid choice, please enter a number between 1 and 6.\n")


if __name__ == "__main__":
    main()
