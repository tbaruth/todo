insert into todo_user (id, username, first_name, last_name, email, dark_mode)
values (1, 'bob', 'Bob', 'Smith', 'bob@test.com', true);

insert into todo_list (id, title, created, updated, user_id)
values (1, 'Test TODO List', '2024-09-22 00:00:00', '2024-09-22 06:00:00', 1);

insert into todo_item (id, title, description, created, updated, status, list_id)
values (1, 'Test Item', 'This is a test.  Lorem ipsum!', '2024-09-22 00:00:00', '2024-09-22 06:00:00', 1, 1),
       (2, 'Test Item 2', 'This is a test.  Lorem ipsum!', '2024-09-22 00:00:00', '2024-09-22 06:00:00', 3, 1);
