create table task_groups
(
    id          int primary key not null auto_increment,
    description varchar(100)    not null,
    done        bit,
    created_at datetime null,
    updated_at datetime null
);

alter table tasks
    add column task_group_id int null;

alter table tasks
    add foreign key (task_group_id) references task_groups(id);