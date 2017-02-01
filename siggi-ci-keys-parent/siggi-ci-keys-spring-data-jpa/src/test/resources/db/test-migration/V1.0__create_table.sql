CREATE TABLE deploy_key_entity (
  id            text NOT NULL  PRIMARY KEY,
  public_key    text NOT NULL,
  private_key   text NOT NULL,
  fingerprint   text NOT NULL
);