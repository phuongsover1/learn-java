insert into ecomm."user" (id, username, password, first_name, last_name, email, phone, user_status) values('a1b9b31d-e73c-4112-af7c-b68530f38222', 'test', 'pwd', 'Test', 'User', 'test@user.com', '234234234', 'ACTIVE');
insert into ecomm."user" (id, username, password, first_name, last_name, email, phone, user_status) values('a1b9b31d-e73c-4112-af7c-b68530f38223', 'test', 'pwd', 'Test2', 'User2', 'test2@user.com', '234234234', 'ACTIVE');
INSERT INTO ecomm.address VALUES ('a731fda1-aaad-42ea-bdbc-a27eeebe2cc0', '9I-999', 'Fraser Suites Le Claridge', 'Champs-Elysees', 'Paris', 'Île-de-France', 'France', '75008');
insert into ecomm.user_address values ('a1b9b31d-e73c-4112-af7c-b68530f38222', 'a731fda1-aaad-42ea-bdbc-a27eeebe2cc0');
INSERT INTO ecomm.card VALUES ('618ffaff-cbcd-48d4-8848-a15601e6725b', '999-999-999-999', 'a1b9b31d-e73c-4112-af7c-b68530f38222', 'User', '12/28', '0000');
insert into ecomm.cart values ('cacab31d-e73c-4112-af7c-b68530f38222', 'a1b9b31d-e73c-4112-af7c-b68530f38222');
insert into ecomm.cart values ('cacab31d-e73c-4112-af7c-b68530f38223', 'a1b9b31d-e73c-4112-af7c-b68530f38223');
insert into ecomm.item values('a7384042-e4aa-4c93-85ae-31a346dad702', '6d62d909-f957-430e-8689-b5129c0bb75e', 1, 17.15);
insert into ecomm.cart_item values ('cacab31d-e73c-4112-af7c-b68530f38222', 'a7384042-e4aa-4c93-85ae-31a346dad702');
insert into ecomm.item values('a7384042-e4aa-4c93-85ae-31a346dad703', 'd3588630-ad8e-49df-bbd7-3167f7efb246', 1, 10.99);
insert into ecomm.cart_item values ('cacab31d-e73c-4112-af7c-b68530f38222', 'a7384042-e4aa-4c93-85ae-31a346dad703');
insert into ecomm.orders(id, customer_id, address_id, card_id, order_date, total, payment_id, shipment_id, status) values ('0a59ba9f-629e-4445-8129-b9bce1985d6a','a1b9b31d-e73c-4112-af7c-b68530f38222', 'a731fda1-aaad-42ea-bdbc-a27eeebe2cc0', '618ffaff-cbcd-48d4-8848-a15601e6725b', current_timestamp, 38.14, NULL, NULL, 'CREATED');
INSERT INTO ecomm.item VALUES
                           ('a7384042-e4aa-4c93-85ae-31a346dad704', '6d62d909-f957-430e-8689-b5129c0bb75e', 1, 17.15),
                           ('a7384042-e4aa-4c93-85ae-31a346dad705', '3395a42e-2d88-40de-b95f-e00e1502085b', 1, 20.99);
INSERT INTO ecomm.order_item VALUES
                                 ('66682caa-a6d8-46ed-a173-ff822f754e1c', '0a59ba9f-629e-4445-8129-b9bce1985d6a', 'a7384042-e4aa-4c93-85ae-31a346dad704'),
                                 ('efeefa71-2760-412a-9ec8-0a040d90f02c', '0a59ba9f-629e-4445-8129-b9bce1985d6a', 'a7384042-e4aa-4c93-85ae-31a346dad705');