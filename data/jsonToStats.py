import json
from datetime import datetime, timedelta
import markdown
import os
import matplotlib.pyplot as plt

filenames = ["mcd1", "mcd2", "sim-stne", "sim-stne-hos", "sim-stne-tos"]
filenames_extended = ["extended-stne", "extended-stne-hos", "extended-stne-tos"]

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

sales_volume_total_list = []
customers_lost_total_list = []
sales_volume_lost_total_list = []

sales_volume_lists_rising_sum = []
sales_volume_lists_rising_sum_lost = []

queue_lists = []
order_lists = []    
pickup_queue_lists = []
pickup_lists = []
exit_lists = []
customers_lost_lists = []

queue_deltas_lists = []
queue_deltas_sideways_lists = []
order_deltas_lists = []
pickup_queue_deltas_lists = []
pickup_deltas_lists = []

sales_volume_lists = []
sales_volume_lost_lists = []

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

    queue = lists[0]
    order = lists[1]
    pickup_queue = lists[2]
    pickup = lists[3]
    exit_list = lists[4]
    customers_lost_list = lists[5]

    queue_lists.append(lists[0])
    order_lists.append(lists[1])
    pickup_queue_lists.append(lists[2])
    pickup_lists.append(lists[3])
    exit_lists.append(lists[4])
    customers_lost_lists.append(lists[5])

    queue_deltas = []
    queue_deltas_sideways = []
    order_deltas = []
    pickup_queue_deltas = []
    pickup_deltas = []

    # Calculate min/max of new arrivals
    for i in range(len(queue)-1):
      try:
        if not queue[i] == "null" and not queue[i+1] == "null":
          delta = queue[i+1] - queue[i]
          #if delta > timedelta(seconds=5):
          queue_deltas_sideways.append(delta)
      except:
        break
    queue_deltas_sideways_lists.append(queue_deltas_sideways)
    max_queue_delta_sideways.append(max(queue_deltas_sideways))
    min_queue_delta_sideways.append(min(queue_deltas_sideways))
    median_queue_delta_sideways.append(sorted(queue_deltas_sideways)[len(queue_deltas_sideways)//2])
    avg_queue_delta_sideways.append(sum(queue_deltas_sideways, timedelta(0)) / len(queue_deltas_sideways))

    # Calculate min/max of each station
    # Queue
    for i in range(len(queue)):
      try:
        if not queue[i] == "null" and not order[i] == "null":
          delta = order[i] - queue[i]
          #if delta > timedelta(seconds=5):
          queue_deltas.append(delta)
      except:
        break
    queue_deltas_lists.append(queue_deltas)
    max_queue_delta.append(max(queue_deltas))
    min_queue_delta.append(min(queue_deltas))
    median_queue_delta.append(sorted(queue_deltas)[len(queue_deltas)//2])
    avg_queue_delta.append(sum(queue_deltas, timedelta(0)) / len(queue_deltas))

    # Order
    for i in range(len(order)):
      try:
        if not order[i] == "null" and not pickup_queue[i] == "null":
          delta = pickup_queue[i] - order[i]
          #if delta > timedelta(seconds=5):
          order_deltas.append(delta)
      except:
        break
    order_deltas_lists.append(order_deltas)
    max_order_delta.append(max(order_deltas))
    min_order_delta.append(min(order_deltas))
    median_order_delta.append(sorted(order_deltas)[len(order_deltas)//2])
    avg_order_delta.append(sum(order_deltas, timedelta(0)) / len(order_deltas))

    # Pickup Queue
    for i in range(len(pickup_queue)):
      try:
        if not pickup_queue[i] == "null" and not pickup[i] == "null":
          delta = pickup[i] - pickup_queue[i]
          #if delta > timedelta(seconds=5):
          pickup_queue_deltas.append(delta)
      except:
        break
    pickup_queue_deltas_lists.append(pickup_deltas)
    max_pickup_queue_delta.append(max(pickup_queue_deltas))
    min_pickup_queue_delta.append(min(pickup_queue_deltas))
    median_pickup_queue_delta.append(sorted(pickup_queue_deltas)[len(pickup_queue_deltas)//2])
    avg_pickup_queue_delta.append(sum(pickup_queue_deltas, timedelta(0)) / len(pickup_queue_deltas))

    # Pickup
    for i in range(len(pickup)):
      try:
        if not pickup[i] == "null" and not exit_list[i] == "null":
          delta = exit_list[i] - pickup[i]
          #if delta > timedelta(seconds=5):
          pickup_deltas.append(delta)
      except:
        break
    pickup_deltas_lists.append(pickup_deltas)
    max_pickup_delta.append(max(pickup_deltas))
    min_pickup_delta.append(min(pickup_deltas))
    median_pickup_delta.append(sorted(pickup_deltas)[len(pickup_deltas)//2])
    avg_pickup_delta.append(sum(pickup_deltas, timedelta(0)) / len(pickup_deltas))


    customers_lost_total_list.append(len(customers_lost_list))

for k in range(len(filenames_extended)):
  with open("raw/" + filenames_extended[k] + ".json") as f:
    json_data_extended = json.load(f)

  lists_extended = []
  for json_list in json_data_extended:
    double_list = []
    for str in json_list:
      double_list.append(float(str))
    lists_extended.append(double_list)

  sales_volume_list = lists_extended[0]
  sales_volume_lost_list = lists_extended[1]

  sales_volume_total_list.append(round(sum(sales_volume_list), 2))
  sales_volume_lost_total_list.append(round(sum(sales_volume_lost_list), 2))

def graphCustomersLost(filenameAdd):
  # Graph customers lost
  plt.figure(figsize=(50, 25))
  plt.rcParams['figure.dpi'] = 1500
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
  plt.savefig("graphs/customers_lost"+filenameAdd+".png")

def graphCustomersExit(filenameAdd):
  plt.figure(figsize=(50, 25))
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
  plt.savefig("graphs/customers_exit"+filenameAdd+".png")

def graphCustomersArrival(filename):
  plt.figure(figsize=(50, 25))
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
  plt.savefig("graphs/customer_arrivals"+filenameAdd+".png")

def writeToReadme():
    # Create a markdown string with the variables and descriptions as a table
    html_string = f"""
  <h1>Data</h1>
  <table>
  <tr>
    <td></td>"""
    for filename in filenames:
        html_string += f"""
    <td>{filename}</td>"""
    html_string += f"""
  </tr>"""
    html_string += f"""
  <tr>
    <td>New Arrival</td>"""
    for i in range(len(filenames)):
      html_string += f"""
    <td>
      <table>
        <tr>
          <td>{min_queue_delta_sideways[i]}</td>
        </tr>
        <tr>
          <td>{avg_queue_delta_sideways[i]}</td>
        </tr>
        <tr>
          <td>{max_queue_delta_sideways[i]}</td>
        </tr>
      </table>
    </td>"""
    html_string += f"""
  </tr>
  <tr>
    <td>Queue</td>"""
    for i in range(len(filenames)):
      html_string += f"""
    <td>
      <table>
        <tr>
          <td>{min_queue_delta[i]}</td>
        </tr>
        <tr>
          <td>{avg_queue_delta[i]}</td>
        </tr>
        <tr>
          <td>{max_queue_delta[i]}</td>
        </tr>
      </table>
    </td>"""
    html_string += f"""
  </tr>
  <tr>
    <td>Order</td>"""
    for i in range(len(filenames)):
      html_string += f"""
    <td>
      <table>
        <tr>
          <td>{min_order_delta[i]}</td>
        </tr>
        <tr>
          <td>{avg_order_delta[i]}</td>
        </tr>
        <tr>
          <td>{max_order_delta[i]}</td>
        </tr>
      </table>
    </td>"""
    html_string += f"""
  </tr>
  <tr>
    <td>Pickup Queue</td>"""
    for i in range(len(filenames)):
      html_string += f"""
    <td>
      <table>
        <tr>
          <td>{min_pickup_queue_delta[i]}</td>
        </tr>
        <tr>
          <td>{avg_pickup_queue_delta[i]}</td>
        </tr>
        <tr>
          <td>{max_pickup_queue_delta[i]}</td>
        </tr>
      </table>
    </td>"""
    html_string += f"""
  </tr>
  <tr>
    <td>Pickup</td>"""
    for i in range(len(filenames)):
      html_string += f"""
    <td>
      <table>
        <tr>
          <td>{min_pickup_delta[i]}</td>
        </tr>
        <tr>
          <td>{avg_pickup_delta[i]}</td>
        </tr>
        <tr>
          <td>{max_pickup_delta[i]}</td>
        </tr>
      </table>
    </td>"""
    html_string += f"""
  </tr>
  <tr>
    <td>Sales Volume</td>"""
    for i in range(len(filenames)):
      if i < 2:
        html_string += f"""
          <td>--</td>"""
      else:
        html_string += f"""
          <td>{sales_volume_total_list[i-2]}</td>"""
    html_string += f"""
  </tr>
  <tr>
    <td>Customers Lost</td>"""
    for i in range(len(filenames)):
      if i < 2:
        html_string += f"""
          <td>--</td>"""
      else:
        html_string += f"""
          <td>{customers_lost_total_list[i]}</td>"""
    html_string += f"""
  </tr>
  <tr>
    <td>Sales Volume Lost</td>"""
    for i in range(len(filenames)):
      if i < 2:
        html_string += f"""
          <td>--</td>"""
      else:
        html_string += f"""
          <td>{sales_volume_lost_total_list[i-2]}</td>"""
    html_string += f"""
  </tr>
  </table>
    """

    html_string += f"""
  <table>
    <tr>
      <td>Legend</td>
      <td>
        <table>
          <tr>
            <td>Min</td>
          </tr>
          <tr>
            <td>Avg</td>
          </tr>
          <tr>
            <td>Max</td>
          </tr>
        </table>
      </td>
    </tr>
  </table>
  """

    html_string += f"""
  <h1>Graphs</h1>"""
    for file in os.listdir("graphs"):
      if file.endswith(".png"):
        html_string += f"""
  <img src="graphs/{file}" alt="{file}">"""


    # Write the HTML string to the README.md file
    with open("README.md", "w") as f:
        f.write(html_string)

writeToReadme()
#graphCustomersArrival()
graphCustomersExit("-default")
graphCustomersLost("-default")
#graphOrderQueueLength()