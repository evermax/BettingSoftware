--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- Name: bet; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE bet (
    idbet integer NOT NULL,
    idsubscriber integer NOT NULL,
    idcompetition integer NOT NULL,
    tokens integer,
    idcompetitor1 integer NOT NULL,
    idcompetitor2 integer,
    idcompetitor3 integer
);


--
-- Name: bet_idbet_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idbet_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bet_idbet_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idbet_seq OWNED BY bet.idbet;


--
-- Name: bet_idcompetition_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idcompetition_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bet_idcompetition_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idcompetition_seq OWNED BY bet.idcompetition;


--
-- Name: bet_idcompetitor1_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idcompetitor1_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bet_idcompetitor1_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idcompetitor1_seq OWNED BY bet.idcompetitor1;


--
-- Name: bet_idcompetitor2_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idcompetitor2_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bet_idcompetitor2_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idcompetitor2_seq OWNED BY bet.idcompetitor2;


--
-- Name: bet_idcompetitor3_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idcompetitor3_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bet_idcompetitor3_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idcompetitor3_seq OWNED BY bet.idcompetitor3;


--
-- Name: bet_idsubscriber_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idsubscriber_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bet_idsubscriber_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idsubscriber_seq OWNED BY bet.idsubscriber;


--
-- Name: competition; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE competition (
    idcompetition integer NOT NULL,
    name text,
    closingdate date,
    settled boolean
);


--
-- Name: competition_idcompetition_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competition_idcompetition_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competition_idcompetition_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competition_idcompetition_seq OWNED BY competition.idcompetition;


--
-- Name: competitionparticipants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE competitionparticipants (
    idcompetition integer NOT NULL,
    idcompetitor integer NOT NULL
);


--
-- Name: competitionparticipants_idcompetition_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitionparticipants_idcompetition_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competitionparticipants_idcompetition_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitionparticipants_idcompetition_seq OWNED BY competitionparticipants.idcompetition;


--
-- Name: competitionparticipants_idcompetitor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitionparticipants_idcompetitor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competitionparticipants_idcompetitor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitionparticipants_idcompetitor_seq OWNED BY competitionparticipants.idcompetitor;


--
-- Name: competitionranking; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE competitionranking (
    idcompetition integer NOT NULL,
    ranking integer NOT NULL,
    idcompetitor integer NOT NULL
);


--
-- Name: competitionranking_idcompetition_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitionranking_idcompetition_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competitionranking_idcompetition_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitionranking_idcompetition_seq OWNED BY competitionranking.idcompetition;


--
-- Name: competitionranking_idcompetitor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitionranking_idcompetitor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competitionranking_idcompetitor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitionranking_idcompetitor_seq OWNED BY competitionranking.idcompetitor;


--
-- Name: competitor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE competitor (
    idcompetitor integer NOT NULL,
    name text,
    firstname text,
    birthdate date,
    isteam boolean
);


--
-- Name: competitor_idcompetitor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitor_idcompetitor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: competitor_idcompetitor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitor_idcompetitor_seq OWNED BY competitor.idcompetitor;


--
-- Name: subscriber; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE subscriber (
    idsubscriber integer NOT NULL,
    username text,
    firstname text,
    lastname text,
    password text,
    tokens integer,
    birthdate date
);


--
-- Name: subscriber_idsubscriber_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE subscriber_idsubscriber_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: subscriber_idsubscriber_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE subscriber_idsubscriber_seq OWNED BY subscriber.idsubscriber;


--
-- Name: teammembers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE teammembers (
    idteam integer NOT NULL,
    idcompetitor integer NOT NULL
);


--
-- Name: teammembers_idcompetitor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE teammembers_idcompetitor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: teammembers_idcompetitor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE teammembers_idcompetitor_seq OWNED BY teammembers.idcompetitor;


--
-- Name: teammembers_idteam_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE teammembers_idteam_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: teammembers_idteam_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE teammembers_idteam_seq OWNED BY teammembers.idteam;


--
-- Name: idbet; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idbet SET DEFAULT nextval('bet_idbet_seq'::regclass);


--
-- Name: idsubscriber; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idsubscriber SET DEFAULT nextval('bet_idsubscriber_seq'::regclass);


--
-- Name: idcompetition; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idcompetition SET DEFAULT nextval('bet_idcompetition_seq'::regclass);


--
-- Name: idcompetitor1; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idcompetitor1 SET DEFAULT nextval('bet_idcompetitor1_seq'::regclass);


--
-- Name: idcompetitor2; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idcompetitor2 SET DEFAULT nextval('bet_idcompetitor2_seq'::regclass);


--
-- Name: idcompetitor3; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idcompetitor3 SET DEFAULT nextval('bet_idcompetitor3_seq'::regclass);


--
-- Name: idcompetition; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competition ALTER COLUMN idcompetition SET DEFAULT nextval('competition_idcompetition_seq'::regclass);


--
-- Name: idcompetition; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants ALTER COLUMN idcompetition SET DEFAULT nextval('competitionparticipants_idcompetition_seq'::regclass);


--
-- Name: idcompetitor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants ALTER COLUMN idcompetitor SET DEFAULT nextval('competitionparticipants_idcompetitor_seq'::regclass);


--
-- Name: idcompetition; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking ALTER COLUMN idcompetition SET DEFAULT nextval('competitionranking_idcompetition_seq'::regclass);


--
-- Name: idcompetitor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking ALTER COLUMN idcompetitor SET DEFAULT nextval('competitionranking_idcompetitor_seq'::regclass);


--
-- Name: idcompetitor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitor ALTER COLUMN idcompetitor SET DEFAULT nextval('competitor_idcompetitor_seq'::regclass);


--
-- Name: idsubscriber; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY subscriber ALTER COLUMN idsubscriber SET DEFAULT nextval('subscriber_idsubscriber_seq'::regclass);


--
-- Name: idteam; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY teammembers ALTER COLUMN idteam SET DEFAULT nextval('teammembers_idteam_seq'::regclass);


--
-- Name: idcompetitor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY teammembers ALTER COLUMN idcompetitor SET DEFAULT nextval('teammembers_idcompetitor_seq'::regclass);


--
-- Name: bet_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_pk PRIMARY KEY (idbet);


--
-- Name: competition_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competition
    ADD CONSTRAINT competition_pk PRIMARY KEY (idcompetition);


--
-- Name: competitionparticipants_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants
    ADD CONSTRAINT competitionparticipants_pk PRIMARY KEY (idcompetition, idcompetitor);


--
-- Name: competitor_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitor
    ADD CONSTRAINT competitor_pk PRIMARY KEY (idcompetitor);


--
-- Name: competitorranking_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking
    ADD CONSTRAINT competitorranking_pk PRIMARY KEY (idcompetition, ranking);


--
-- Name: subscriber_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY subscriber
    ADD CONSTRAINT subscriber_pk PRIMARY KEY (idsubscriber);


--
-- Name: teammembers_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY teammembers
    ADD CONSTRAINT teammembers_pk PRIMARY KEY (idteam, idcompetitor);


--
-- Name: unique_competitor_constraint; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitor
    ADD CONSTRAINT unique_competitor_constraint UNIQUE (name, firstname, birthdate);


--
-- Name: unique_subscriber_constraint; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY subscriber
    ADD CONSTRAINT unique_subscriber_constraint UNIQUE (username);


--
-- Name: fki_bet_competition_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_bet_competition_fk ON bet USING btree (idcompetition);


--
-- Name: fki_bet_subscriber_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_bet_subscriber_fk ON bet USING btree (idsubscriber);


--
-- Name: fki_competitionparticipants_competitor_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_competitionparticipants_competitor_fk ON competitionparticipants USING btree (idcompetitor);


--
-- Name: fki_competitorranking_competitor_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_competitorranking_competitor_fk ON competitionranking USING btree (idcompetitor);


--
-- Name: bet_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_competition_fk FOREIGN KEY (idcompetition) REFERENCES competition(idcompetition);


--
-- Name: bet_competitor1_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_competitor1_fk FOREIGN KEY (idcompetitor1) REFERENCES competitor(idcompetitor);


--
-- Name: bet_competitor2_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_competitor2_fk FOREIGN KEY (idcompetitor2) REFERENCES competitor(idcompetitor);


--
-- Name: bet_competitor3_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_competitor3_fk FOREIGN KEY (idcompetitor3) REFERENCES competitor(idcompetitor);


--
-- Name: bet_subscriber_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_subscriber_fk FOREIGN KEY (idsubscriber) REFERENCES subscriber(idsubscriber);


--
-- Name: competitionparticipants_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants
    ADD CONSTRAINT competitionparticipants_competition_fk FOREIGN KEY (idcompetition) REFERENCES competition(idcompetition);


--
-- Name: competitionparticipants_competitor_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants
    ADD CONSTRAINT competitionparticipants_competitor_fk FOREIGN KEY (idcompetitor) REFERENCES competitor(idcompetitor);


--
-- Name: competitorranking_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking
    ADD CONSTRAINT competitorranking_competition_fk FOREIGN KEY (idcompetition) REFERENCES competition(idcompetition);


--
-- Name: competitorranking_competitor_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking
    ADD CONSTRAINT competitorranking_competitor_fk FOREIGN KEY (idcompetitor) REFERENCES competitor(idcompetitor);


--
-- PostgreSQL database dump complete
--

