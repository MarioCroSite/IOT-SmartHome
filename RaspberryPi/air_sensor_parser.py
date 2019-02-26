import json

def parse_bytes_into_object(sensor_output_bytes):
    linesStr = sensor_output_bytes.decode()
    linesList = linesStr.split("\n")
    del linesList[0]
    del linesList[3]
    linesList[1] = linesList[1][1::]
    linesList[2] = linesList[2][1::]

    obj = {}
    for s in linesList:
        pair = s.split(": ")
        obj[pair[0]] = pair[1]

    relHumidity = obj["rel. humidity"]
    del obj["rel. humidity"]
    obj["relativeHumidity"] = relHumidity

    for key in obj:
        pair = obj[key].split(" ")
        obj[key] = float(pair[0])

    return obj

def parse_bytes_into_json(sensor_output_bytes):
    return json.dumps(parse_bytes_into_object(sensor_output_bytes))