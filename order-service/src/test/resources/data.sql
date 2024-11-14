INSERT INTO orders (
    username,
    cost,
    creation_time,
    modified_time,
    departure_address,
    destination_address,
    description,
    status
) VALUES (
             'User 1',
             100,
             NOW(),
             NOW(),
             'Departure address',
             'Destination address',
             'First order',
             'REGISTERED'
         ), (
             'User 1',
             200,
             NOW(),
             NOW(),
             'Departure address',
             'Destination address',
             'Second order',
             'REGISTERED'
         );