CREATE TABLE IF NOT EXISTS public.customer
(
    Id SERIAL PRIMARY KEY,
    name_cust VARCHAR(20),
    addres_cust VARCHAR(20),
    summ INTEGER,
    date DATE
);

CREATE TABLE IF NOT EXISTS public.orders
(
    Id SERIAL,
    num_product VARCHAR(20),
    name_product VARCHAR(20),
    quantity_product INTEGER,
    customerId integer,
    PRIMARY KEY(Id, customerId),
    FOREIGN KEY (customerId) REFERENCES public.customer (Id) ON DELETE CASCADE
);