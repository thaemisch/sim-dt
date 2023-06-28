import json
from datetime import datetime, timedelta
import matplotlib.pyplot as plt

filenames = ["mcd1", "mcd2", "sim-stne", "sim-stne-hos", "sim-stne-tos"]
filenames_extended = ["extended-stne", "extended-stne-hos", "extended-stne-tos"]

queue_lists = []
order_lists = []    
pickup_queue_lists = []
pickup_lists = []
exit_lists = []
customers_lost_lists = []

sales_volume_lists = []
sales_volume_lost_lists = []


max_queue_delta_sideways = []
min_queue_delta_sideways = []
median_queue_delta_sideways = []
avg_queue_delta_sideways = []

max_queue_delta = []
min_queue_delta = []
median_queue_delta = []
avg_queue_delta = []

max_order_delta = []
min_order_delta = []
median_order_delta = []
avg_order_delta = []

max_pickup_queue_delta = []
min_pickup_queue_delta = []
median_pickup_queue_delta = []
avg_pickup_queue_delta = []

max_pickup_delta = []
min_pickup_delta = []
median_pickup_delta = []
avg_pickup_delta = []

sales_volume_lists_rising_sum = []
sales_volume_lists_rising_sum_lost = []

for j in range(len(filenames)):
    # Read the JSON file
    with open("raw/" + filenames[j] + ".json") as f:
        json_data = json.load(f)

    # Convert the JSON data to lists of datetimes
    lists = []
    for json_list in json_data:
        dt_list = []
        for dt_str in json_list:
          try:
            dt_list.append(datetime.strptime(dt_str, "%H:%M:%S"))
          except:
            print("Error parsing datetime: " + dt_str)
            break
        lists.append(dt_list)

    queue_lists.append(lists[0])
    order_lists.append(lists[1])
    pickup_queue_lists.append(lists[2])
    pickup_lists.append(lists[3])
    exit_lists.append(lists[4])
    customers_lost_lists.append(lists[5])

for k in range(len(filenames_extended)):
  with open("raw/" + filenames_extended[k] + ".json") as f:
    json_data_extended = json.load(f)

  lists_extended = []
  for json_list in json_data_extended:
    double_list = []
    for str in json_list:
      double_list.append(float(str))
    lists_extended.append(double_list)

  sales_volume_lists.append(lists_extended[0])
  sales_volume_lost_lists.append(lists_extended[1])

# Graph customers lost
plt.figure(figsize=(10, 5))
for k in range(len(filenames_extended)):
    x = []
    y = []
    counter = 0
    for l in range(len(customers_lost_lists[k+2])):
      x.append(customers_lost_lists[k+2][l])
      y.append(l)
    plt.plot(x, y, label=filenames_extended[k].replace("extended", "sim"))

plt.title("Customers lost")
plt.xlabel("Time")
plt.ylabel("Customers")
plt.legend()
plt.savefig("graphs/customers_lost.png")

# Graph customers exit rate
plt.figure(figsize=(10, 5))
for k in range(len(filenames_extended)):
    x = []
    y = []
    counter = 0
    for l in range(len(exit_lists[k+2])):
      x.append(exit_lists[k+2][l])
      y.append(l)
    plt.plot(x, y, label=filenames_extended[k].replace("extended", "sim"))

plt.title("Customers exit")
plt.xlabel("Time")
plt.ylabel("Customers")
plt.legend()
plt.savefig("graphs/customers_exit.png")

# Graph customer arrivals
plt.figure(figsize=(10, 5))
for k in range(len(filenames_extended)):
    x = []
    y = []
    counter = 0
    for l in range(len(queue_lists[k+2])):
      x.append(queue_lists[k+2][l])
      y.append(l)
    plt.plot(x, y, label=filenames_extended[k].replace("extended", "sim"))

plt.title("Customer arrivals")
plt.xlabel("Time")
plt.ylabel("Customers")
plt.legend()
plt.savefig("graphs/customer_arrivals.png")


