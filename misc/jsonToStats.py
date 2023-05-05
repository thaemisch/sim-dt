import json

# Read the JSON file
with open('lists.json') as f:
    json_data = json.load(f)

# Convert the JSON data to lists of datetimes
lists = []
for json_list in json_data:
    dt_list = [datetime.fromisoformat(dt_str) for dt_str in json_list]
    lists.append(dt_list)

# Use the lists in your Python program
for lst in lists:
    print(lst)
