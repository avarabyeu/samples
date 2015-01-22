DROP TABLE title IF EXISTS;
CREATE TABLE title (
  id INTEGER IDENTITY,
  label VARCHAR(255),
  type VARCHAR(255),
  format VARCHAR(255),
  date DATETIME,
  person_id INTEGER,
  promo_id INTEGER
);

DROP TABLE person IF EXISTS;
CREATE TABLE person (
  id INTEGER IDENTITY,
  name VARCHAR(255));

DROP TABLE promo IF EXISTS;
CREATE TABLE promo (
  id INTEGER IDENTITY,
  label VARCHAR(255));


ALTER TABLE title ADD FOREIGN KEY (person_id) REFERENCES person(id) ON DELETE CASCADE;
ALTER TABLE title ADD FOREIGN KEY (promo_id) REFERENCES promo(id);

-- ----------------------------
-- Records of validation
-- ----------------------------
INSERT INTO person VALUES (1, 'Vasia');
INSERT INTO person VALUES (2, 'Petia');
INSERT INTO person VALUES (3, 'Svetlana');

INSERT INTO promo VALUES (1, 'promo1');
INSERT INTO promo VALUES (2, 'promo2');
INSERT INTO promo VALUES (3, 'promo3');

INSERT INTO title VALUES (1, 'title1', 'type1', 'format1', '2014-10-14 17:31:10', 1, 1);
INSERT INTO title VALUES (2, 'title2', 'type2', 'format2', '2014-10-14 17:31:10', 2, 2);

