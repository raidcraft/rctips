-- apply changes
create table rctips_accepted (
  id                            varchar(40) not null,
  player                        varchar(40),
  tip_id                        varchar(255),
  version                       integer not null,
  when_created                  timestamp not null,
  when_modified                 timestamp not null,
  constraint pk_rctips_accepted primary key (id)
);

