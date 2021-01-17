-- apply changes
create table rctips_accepted (
  id                            varchar(40) not null,
  player                        varchar(40),
  tip_id                        varchar(255),
  version                       bigint not null,
  when_created                  datetime(6) not null,
  when_modified                 datetime(6) not null,
  constraint pk_rctips_accepted primary key (id)
);

