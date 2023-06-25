import json
from datetime import datetime, timedelta
import markdown

filenames = ["mcd1", "mcd2"]

max_queue_delta_sideways = []
min_queue_delta_sideways = []
median_queue_delta_sideways = []

max_queue_delta = []
min_queue_delta = []
median_queue_delta = []

max_order_delta = []
min_order_delta = []
median_order_delta = []

max_pickup_queue_delta = []
min_pickup_queue_delta = []
median_pickup_queue_delta = []

max_pickup_delta = []
min_pickup_delta = []
median_pickup_delta = []


for j in range(len(filenames)):
    # Read the JSON file
    with open(filenames[j] + ".json") as f:
        json_data = json.load(f)

    # Convert the JSON data to lists of datetimes
    lists = []
    for json_list in json_data:
        dt_list = [datetime.strptime(dt_str, "%H:%M:%S") for dt_str in json_list]
        lists.append(dt_list)

    queue = lists[0]
    order = lists[1]
    pickup_queue = lists[2]
    pickup = lists[3]
    exit = lists[4]

    queue_deltas = []
    queue_deltas_sideways = []
    order_deltas = []
    pickup_queue_deltas = []
    pickup_deltas = []

    # Calculate min/max of new arrivals
    for i in range(len(queue)-1):
      delta = queue[i+1] - queue[i]
      if delta > timedelta(seconds=5):
        queue_deltas_sideways.append(delta)
    max_queue_delta_sideways.append(max(queue_deltas_sideways))
    min_queue_delta_sideways.append(min(queue_deltas_sideways))
    median_queue_delta_sideways.append(sorted(queue_deltas_sideways)[len(queue_deltas_sideways)//2])

    # Calculate min/max of each station
    # Queue
    for i in range(len(queue)):
      delta = order[i] - queue[i]
      if delta > timedelta(seconds=5):
        queue_deltas.append(delta)
    max_queue_delta.append(max(queue_deltas))
    min_queue_delta.append(min(queue_deltas))
    median_queue_delta.append(sorted(queue_deltas)[len(queue_deltas)//2])

    # Order
    for i in range(len(order)):
        delta = pickup_queue[i] - order[i]
        if delta > timedelta(seconds=5):
          order_deltas.append(delta)
    max_order_delta.append(max(order_deltas))
    min_order_delta.append(min(order_deltas))
    median_order_delta.append(sorted(order_deltas)[len(order_deltas)//2])

    # Pickup Queue
    for i in range(len(pickup_queue)):
        delta = pickup[i] - pickup_queue[i]
        if delta > timedelta(seconds=5):
          pickup_queue_deltas.append(delta)
    max_pickup_queue_delta.append(max(pickup_queue_deltas))
    min_pickup_queue_delta.append(min(pickup_queue_deltas))
    median_pickup_queue_delta.append(sorted(pickup_queue_deltas)[len(pickup_queue_deltas)//2])

    # Pickup
    for i in range(len(pickup)):
      delta = exit[i] - pickup[i]
      if delta > timedelta(seconds=5):
        pickup_deltas.append(delta)
    max_pickup_delta.append(max(pickup_deltas))
    min_pickup_delta.append(min(pickup_deltas))
    median_pickup_delta.append(sorted(pickup_deltas)[len(pickup_deltas)//2])

def writeToReadme():
  # Create a markdown string with the variables and descriptions as a table
  html_string = f"""
  <table>
  <tr>
    <td></td>
    <td>{filenames[0]}</td>
    <td>{filenames[1]}</td>
  </tr>
  <tr>
    <td>New Arrival</td>
    <td>
      <table>
        <tr>
          <td>{min_queue_delta_sideways[0]}</td>
        </tr>
        <tr>
          <td>{median_queue_delta_sideways[0]}</td>
        </tr>
        <tr>
          <td>{max_queue_delta_sideways[0]}</td>
        </tr>
      </table>
    </td>
    <td>
      <table>
        <tr>
          <td>{min_queue_delta_sideways[1]}</td>
        </tr>
        <tr>
          <td>{median_queue_delta_sideways[1]}</td>
        </tr>
        <tr>
          <td>{max_queue_delta_sideways[1]}</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>Queue</td>
    <td>
      <table>
        <tr>
          <td>{min_queue_delta[0]}</td>
        </tr>
        <tr>
          <td>{median_queue_delta[0]}</td>
        </tr>
        <tr>
          <td>{max_queue_delta[0]}</td>
        </tr>
      </table>
    </td>
    <td>
      <table>
        <tr>
          <td>{min_queue_delta[1]}</td>
        </tr>
        <tr>
          <td>{median_queue_delta[1]}</td>
        </tr>
        <tr>
          <td>{max_queue_delta[1]}</td>
        </tr>
      </table>
    </td>
  </tr>
  <tr>
    <td>Order</td>
    <td>
      <table>
        <tr>
          <td>{min_order_delta[0]}</td>
        </tr>
        <tr>
          <td>{median_order_delta[0]}</td>
        </tr>
        <tr>
          <td>{max_order_delta[0]}</td>
        </tr>
      </table>
    </td>
    <td>
      <table>
        <tr>
          <td>{min_order_delta[1]}</td>
        </tr>
        <tr>
          <td>{median_order_delta[1]}</td>
        </tr>
        <tr>
          <td>{max_order_delta[1]}</td>
        </tr>
      </table>
    </td>
  </tr>
    <tr>
    <td>Pickup Queue</td>
    <td>
      <table>
        <tr>
          <td>{min_pickup_queue_delta[0]}</td>
        </tr>
        <tr>
          <td>{median_pickup_queue_delta[0]}</td>
        </tr>
        <tr>
          <td>{max_pickup_queue_delta[0]}</td>
        </tr>
      </table>
    </td>
    <td>
      <table>
        <tr>
          <td>{min_pickup_queue_delta[1]}</td>
        </tr>
        <tr>
          <td>{median_pickup_queue_delta[1]}</td>
        </tr>
        <tr>
          <td>{max_pickup_queue_delta[1]}</td>
        </tr>
      </table>
    </td>
  </tr>
    <tr>
    <td>Pickup</td>
    <td>
      <table>
        <tr>
          <td>{min_pickup_delta[0]}</td>
        </tr>
        <tr>
          <td>{median_pickup_delta[0]}</td>
        </tr>
        <tr>
          <td>{max_pickup_delta[0]}</td>
        </tr>
      </table>
    </td>
    <td>
      <table>
        <tr>
          <td>{min_pickup_delta[1]}</td>
        </tr>
        <tr>
          <td>{median_pickup_delta[1]}</td>
        </tr>
        <tr>
          <td>{max_pickup_delta[1]}</td>
        </tr>
      </table>
    </td>
  </tr>
  </table>
  """

  # Write the HTML string to the output.html file
  with open("README.md", "w") as f:
    f.write(html_string)


writeToReadme()