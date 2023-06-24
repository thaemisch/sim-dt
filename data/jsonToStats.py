import json
from datetime import datetime, timedelta
import markdown

filenames = ["mcd1", "mcd2"]

max_queue_delta_sideways = []
min_queue_delta_sideways = []

max_queue_delta = []
min_queue_delta = []

max_order_delta = []
min_order_delta = []

max_pickup_queue_delta = []
min_pickup_queue_delta = []

max_pickup_delta = []
min_pickup_delta = []


for j in range(len(filenames)):
    print(j)


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
        queue_deltas_sideways.append(queue[i+1] - queue[i])
    max_queue_delta_sideways.append(max(queue_deltas_sideways))
    min_queue_delta_sideways.append(min(queue_deltas_sideways))

    # Calculate min/max of each station
    # Queue
    for i in range(len(queue)):
        queue_deltas.append(order[i] - queue[i])
    max_queue_delta.append(max(queue_deltas))
    min_queue_delta.append(min(queue_deltas))

    # Order
    for i in range(len(order)):
        order_deltas.append(pickup_queue[i] - order[i])
    max_order_delta.append(max(order_deltas))
    min_order_delta.append(min(order_deltas))

    # Pickup Queue
    for i in range(len(pickup_queue)):
        pickup_queue_deltas.append(pickup[i] - pickup_queue[i])
    max_pickup_queue_delta.append(max(pickup_queue_deltas))
    min_pickup_queue_delta.append(min(pickup_queue_deltas))

    # Pickup
    for i in range(len(pickup)):
        pickup_deltas.append(exit[i] - pickup[i])
    max_pickup_delta.append(max(pickup_deltas))
    min_pickup_delta.append(min(pickup_deltas))

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