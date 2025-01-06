CREATE TABLE IF NOT EXISTS categories (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  name VARCHAR(50)                           NOT NULL,
  CONSTRAINT pk_categories PRIMARY KEY (id),
  CONSTRAINT UQ_CATEGORYNAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS compilations (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  pinned BOOLEAN                             NOT NULL,
  title VARCHAR(50)                          NOT NULL,
  CONSTRAINT pk_compilations PRIMARY KEY (id),
  CONSTRAINT UQ_COMPILATIONSTITLE UNIQUE (title)
);

CREATE TABLE IF NOT EXISTS users (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  email VARCHAR(254)                         NOT NULL,
  name VARCHAR(250)                          NOT NULL,
  CONSTRAINT pk_users PRIMARY KEY (id),
  CONSTRAINT UQ_USEREMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS events (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  annotation VARCHAR(2000)                   NOT NULL,
  category_id BIGINT                         NOT NULL,
  confirmed_requests BIGINT,
  created_on TIMESTAMP WITHOUT TIME ZONE     NOT NULL,
  description varchar(7000)                  NOT NULL,
  event_date TIMESTAMP WITHOUT TIME ZONE     NOT NULL,
  initiator_id BIGINT                        NOT NULL,
  lat REAL                                   NOT NULL,
  lon REAL                                   NOT NULL,
  paid BOOLEAN                               NOT NULL,
  participant_limit BIGINT,
  published_on TIMESTAMP WITHOUT TIME ZONE,
  request_moderation BOOLEAN,
  state VARCHAR(10)                          NOT NULL,
  title VARCHAR(120)                         NOT NULL,
  views BIGINT,
  CONSTRAINT pk_events PRIMARY KEY (id),
  FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE CASCADE,
  FOREIGN KEY (initiator_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS requests (
  id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
  created TIMESTAMP WITHOUT TIME ZONE        NOT NULL,
  event_id BIGINT                            NOT NULL,
  requester_id BIGINT                        NOT NULL,
  status VARCHAR(10)                         NOT NULL,
  CONSTRAINT pk_requests PRIMARY KEY (id),
  CONSTRAINT UQ_REQUESTEREVENT UNIQUE (event_id, requester_id),
  FOREIGN KEY (event_id) REFERENCES events(id) ON DELETE CASCADE,
  FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS events_compilations (
  compilation_id BIGINT REFERENCES compilations(id) ON DELETE CASCADE,
  event_id BIGINT REFERENCES events(id) ON DELETE CASCADE,
  PRIMARY KEY (compilation_id, event_id),
  CONSTRAINT UQ_EVENT_COMPILATION UNIQUE (compilation_id, event_id)
);
