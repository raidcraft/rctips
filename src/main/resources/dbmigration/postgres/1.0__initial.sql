-- apply changes
create table rctips_accepted (
  id                            uuid not null,
  player                        uuid,
  tip_id                        varchar(255),
  version                       bigint not null,
  when_created                  timestamptz not null,
  when_modified                 timestamptz not null,
  constraint pk_rctips_accepted primary key (id)
);

