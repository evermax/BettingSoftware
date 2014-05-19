--
-- PostgreSQL database dump
--

-- Dumped from database version 9.1.9
-- Dumped by pg_dump version 9.1.9
-- Started on 2014-05-19 11:40:41 CEST

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 183 (class 3079 OID 11654)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1965 (class 0 OID 0)
-- Dependencies: 183
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_with_oids = false;

--
-- TOC entry 167 (class 1259 OID 17266)
-- Dependencies: 5
-- Name: bet; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE bet (
    idbet integer NOT NULL,
    idsubscriber integer NOT NULL,
    idcompetition integer NOT NULL,
    tokens integer,
    idcompetitor1 integer NOT NULL,
    idcompetitor2 integer NOT NULL,
    idcompetitor3 integer NOT NULL
);


--
-- TOC entry 161 (class 1259 OID 17254)
-- Dependencies: 167 5
-- Name: bet_idbet_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idbet_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1966 (class 0 OID 0)
-- Dependencies: 161
-- Name: bet_idbet_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idbet_seq OWNED BY bet.idbet;


--
-- TOC entry 163 (class 1259 OID 17258)
-- Dependencies: 167 5
-- Name: bet_idcompetition_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idcompetition_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1967 (class 0 OID 0)
-- Dependencies: 163
-- Name: bet_idcompetition_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idcompetition_seq OWNED BY bet.idcompetition;


--
-- TOC entry 164 (class 1259 OID 17260)
-- Dependencies: 5 167
-- Name: bet_idcompetitor1_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idcompetitor1_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1968 (class 0 OID 0)
-- Dependencies: 164
-- Name: bet_idcompetitor1_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idcompetitor1_seq OWNED BY bet.idcompetitor1;


--
-- TOC entry 165 (class 1259 OID 17262)
-- Dependencies: 5 167
-- Name: bet_idcompetitor2_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idcompetitor2_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1969 (class 0 OID 0)
-- Dependencies: 165
-- Name: bet_idcompetitor2_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idcompetitor2_seq OWNED BY bet.idcompetitor2;


--
-- TOC entry 166 (class 1259 OID 17264)
-- Dependencies: 167 5
-- Name: bet_idcompetitor3_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idcompetitor3_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1970 (class 0 OID 0)
-- Dependencies: 166
-- Name: bet_idcompetitor3_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idcompetitor3_seq OWNED BY bet.idcompetitor3;


--
-- TOC entry 162 (class 1259 OID 17256)
-- Dependencies: 5 167
-- Name: bet_idsubscriber_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE bet_idsubscriber_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1971 (class 0 OID 0)
-- Dependencies: 162
-- Name: bet_idsubscriber_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE bet_idsubscriber_seq OWNED BY bet.idsubscriber;


--
-- TOC entry 169 (class 1259 OID 17277)
-- Dependencies: 5
-- Name: competition; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE competition (
    idcompetition integer NOT NULL,
    name text,
    closingdate date,
    settled boolean
);


--
-- TOC entry 168 (class 1259 OID 17275)
-- Dependencies: 169 5
-- Name: competition_idcompetition_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competition_idcompetition_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1972 (class 0 OID 0)
-- Dependencies: 168
-- Name: competition_idcompetition_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competition_idcompetition_seq OWNED BY competition.idcompetition;


--
-- TOC entry 172 (class 1259 OID 17288)
-- Dependencies: 5
-- Name: competitionparticipants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE competitionparticipants (
    idcompetition integer NOT NULL,
    idcompetitor integer NOT NULL
);


--
-- TOC entry 170 (class 1259 OID 17284)
-- Dependencies: 5 172
-- Name: competitionparticipants_idcompetition_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitionparticipants_idcompetition_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1973 (class 0 OID 0)
-- Dependencies: 170
-- Name: competitionparticipants_idcompetition_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitionparticipants_idcompetition_seq OWNED BY competitionparticipants.idcompetition;


--
-- TOC entry 171 (class 1259 OID 17286)
-- Dependencies: 5 172
-- Name: competitionparticipants_idcompetitor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitionparticipants_idcompetitor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1974 (class 0 OID 0)
-- Dependencies: 171
-- Name: competitionparticipants_idcompetitor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitionparticipants_idcompetitor_seq OWNED BY competitionparticipants.idcompetitor;


--
-- TOC entry 175 (class 1259 OID 17297)
-- Dependencies: 5
-- Name: competitionranking; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE competitionranking (
    idcompetition integer NOT NULL,
    ranking integer NOT NULL,
    idcompetitor integer NOT NULL
);


--
-- TOC entry 173 (class 1259 OID 17293)
-- Dependencies: 175 5
-- Name: competitionranking_idcompetition_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitionranking_idcompetition_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1975 (class 0 OID 0)
-- Dependencies: 173
-- Name: competitionranking_idcompetition_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitionranking_idcompetition_seq OWNED BY competitionranking.idcompetition;


--
-- TOC entry 174 (class 1259 OID 17295)
-- Dependencies: 5 175
-- Name: competitionranking_idcompetitor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitionranking_idcompetitor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1976 (class 0 OID 0)
-- Dependencies: 174
-- Name: competitionranking_idcompetitor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitionranking_idcompetitor_seq OWNED BY competitionranking.idcompetitor;


--
-- TOC entry 177 (class 1259 OID 17304)
-- Dependencies: 5
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
-- TOC entry 176 (class 1259 OID 17302)
-- Dependencies: 5 177
-- Name: competitor_idcompetitor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE competitor_idcompetitor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1977 (class 0 OID 0)
-- Dependencies: 176
-- Name: competitor_idcompetitor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE competitor_idcompetitor_seq OWNED BY competitor.idcompetitor;


--
-- TOC entry 179 (class 1259 OID 17313)
-- Dependencies: 5
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
-- TOC entry 178 (class 1259 OID 17311)
-- Dependencies: 5 179
-- Name: subscriber_idsubscriber_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE subscriber_idsubscriber_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1978 (class 0 OID 0)
-- Dependencies: 178
-- Name: subscriber_idsubscriber_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE subscriber_idsubscriber_seq OWNED BY subscriber.idsubscriber;


--
-- TOC entry 182 (class 1259 OID 17324)
-- Dependencies: 5
-- Name: teammembers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE teammembers (
    idteam integer NOT NULL,
    idcompetitor integer NOT NULL
);


--
-- TOC entry 181 (class 1259 OID 17322)
-- Dependencies: 182 5
-- Name: teammembers_idcompetitor_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE teammembers_idcompetitor_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1979 (class 0 OID 0)
-- Dependencies: 181
-- Name: teammembers_idcompetitor_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE teammembers_idcompetitor_seq OWNED BY teammembers.idcompetitor;


--
-- TOC entry 180 (class 1259 OID 17320)
-- Dependencies: 182 5
-- Name: teammembers_idteam_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE teammembers_idteam_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- TOC entry 1980 (class 0 OID 0)
-- Dependencies: 180
-- Name: teammembers_idteam_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE teammembers_idteam_seq OWNED BY teammembers.idteam;


--
-- TOC entry 1917 (class 2604 OID 17269)
-- Dependencies: 167 161 167
-- Name: idbet; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idbet SET DEFAULT nextval('bet_idbet_seq'::regclass);


--
-- TOC entry 1918 (class 2604 OID 17270)
-- Dependencies: 162 167 167
-- Name: idsubscriber; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idsubscriber SET DEFAULT nextval('bet_idsubscriber_seq'::regclass);


--
-- TOC entry 1919 (class 2604 OID 17271)
-- Dependencies: 163 167 167
-- Name: idcompetition; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idcompetition SET DEFAULT nextval('bet_idcompetition_seq'::regclass);


--
-- TOC entry 1920 (class 2604 OID 17272)
-- Dependencies: 167 164 167
-- Name: idcompetitor1; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idcompetitor1 SET DEFAULT nextval('bet_idcompetitor1_seq'::regclass);


--
-- TOC entry 1921 (class 2604 OID 17273)
-- Dependencies: 167 165 167
-- Name: idcompetitor2; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idcompetitor2 SET DEFAULT nextval('bet_idcompetitor2_seq'::regclass);


--
-- TOC entry 1922 (class 2604 OID 17274)
-- Dependencies: 166 167 167
-- Name: idcompetitor3; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet ALTER COLUMN idcompetitor3 SET DEFAULT nextval('bet_idcompetitor3_seq'::regclass);


--
-- TOC entry 1923 (class 2604 OID 17280)
-- Dependencies: 169 168 169
-- Name: idcompetition; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competition ALTER COLUMN idcompetition SET DEFAULT nextval('competition_idcompetition_seq'::regclass);


--
-- TOC entry 1924 (class 2604 OID 17291)
-- Dependencies: 170 172 172
-- Name: idcompetition; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants ALTER COLUMN idcompetition SET DEFAULT nextval('competitionparticipants_idcompetition_seq'::regclass);


--
-- TOC entry 1925 (class 2604 OID 17292)
-- Dependencies: 172 171 172
-- Name: idcompetitor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants ALTER COLUMN idcompetitor SET DEFAULT nextval('competitionparticipants_idcompetitor_seq'::regclass);


--
-- TOC entry 1926 (class 2604 OID 17300)
-- Dependencies: 175 173 175
-- Name: idcompetition; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking ALTER COLUMN idcompetition SET DEFAULT nextval('competitionranking_idcompetition_seq'::regclass);


--
-- TOC entry 1927 (class 2604 OID 17301)
-- Dependencies: 174 175 175
-- Name: idcompetitor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking ALTER COLUMN idcompetitor SET DEFAULT nextval('competitionranking_idcompetitor_seq'::regclass);


--
-- TOC entry 1928 (class 2604 OID 17307)
-- Dependencies: 177 176 177
-- Name: idcompetitor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitor ALTER COLUMN idcompetitor SET DEFAULT nextval('competitor_idcompetitor_seq'::regclass);


--
-- TOC entry 1929 (class 2604 OID 17316)
-- Dependencies: 178 179 179
-- Name: idsubscriber; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY subscriber ALTER COLUMN idsubscriber SET DEFAULT nextval('subscriber_idsubscriber_seq'::regclass);


--
-- TOC entry 1930 (class 2604 OID 17327)
-- Dependencies: 180 182 182
-- Name: idteam; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY teammembers ALTER COLUMN idteam SET DEFAULT nextval('teammembers_idteam_seq'::regclass);


--
-- TOC entry 1931 (class 2604 OID 17328)
-- Dependencies: 181 182 182
-- Name: idcompetitor; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY teammembers ALTER COLUMN idcompetitor SET DEFAULT nextval('teammembers_idcompetitor_seq'::regclass);


--
-- TOC entry 1933 (class 2606 OID 17330)
-- Dependencies: 167 167 1960
-- Name: bet_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_pk PRIMARY KEY (idbet);


--
-- TOC entry 1937 (class 2606 OID 17332)
-- Dependencies: 169 169 1960
-- Name: competition_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competition
    ADD CONSTRAINT competition_pk PRIMARY KEY (idcompetition);


--
-- TOC entry 1939 (class 2606 OID 17334)
-- Dependencies: 172 172 172 1960
-- Name: competitionparticipants_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants
    ADD CONSTRAINT competitionparticipants_pk PRIMARY KEY (idcompetition, idcompetitor);


--
-- TOC entry 1945 (class 2606 OID 17336)
-- Dependencies: 177 177 1960
-- Name: competitor_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitor
    ADD CONSTRAINT competitor_pk PRIMARY KEY (idcompetitor);


--
-- TOC entry 1942 (class 2606 OID 17338)
-- Dependencies: 175 175 175 1960
-- Name: competitorranking_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking
    ADD CONSTRAINT competitorranking_pk PRIMARY KEY (idcompetition, ranking);


--
-- TOC entry 1947 (class 2606 OID 17340)
-- Dependencies: 179 179 1960
-- Name: subscriber_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY subscriber
    ADD CONSTRAINT subscriber_pk PRIMARY KEY (idsubscriber);


--
-- TOC entry 1949 (class 2606 OID 17342)
-- Dependencies: 182 182 182 1960
-- Name: teammembers_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY teammembers
    ADD CONSTRAINT teammembers_pk PRIMARY KEY (idteam, idcompetitor);


--
-- TOC entry 1934 (class 1259 OID 17343)
-- Dependencies: 167 1960
-- Name: fki_bet_competition_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_bet_competition_fk ON bet USING btree (idcompetition);


--
-- TOC entry 1935 (class 1259 OID 17344)
-- Dependencies: 167 1960
-- Name: fki_bet_subscriber_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_bet_subscriber_fk ON bet USING btree (idsubscriber);


--
-- TOC entry 1940 (class 1259 OID 17345)
-- Dependencies: 172 1960
-- Name: fki_competitionparticipants_competitor_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_competitionparticipants_competitor_fk ON competitionparticipants USING btree (idcompetitor);


--
-- TOC entry 1943 (class 1259 OID 17346)
-- Dependencies: 175 1960
-- Name: fki_competitorranking_competitor_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_competitorranking_competitor_fk ON competitionranking USING btree (idcompetitor);


--
-- TOC entry 1950 (class 2606 OID 17347)
-- Dependencies: 167 1936 169 1960
-- Name: bet_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_competition_fk FOREIGN KEY (idcompetition) REFERENCES competition(idcompetition);


--
-- TOC entry 1951 (class 2606 OID 17352)
-- Dependencies: 167 177 1944 1960
-- Name: bet_competitor1_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_competitor1_fk FOREIGN KEY (idcompetitor1) REFERENCES competitor(idcompetitor);


--
-- TOC entry 1952 (class 2606 OID 17357)
-- Dependencies: 177 167 1944 1960
-- Name: bet_competitor2_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_competitor2_fk FOREIGN KEY (idcompetitor2) REFERENCES competitor(idcompetitor);


--
-- TOC entry 1953 (class 2606 OID 17362)
-- Dependencies: 1944 167 177 1960
-- Name: bet_competitor3_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_competitor3_fk FOREIGN KEY (idcompetitor3) REFERENCES competitor(idcompetitor);


--
-- TOC entry 1954 (class 2606 OID 17367)
-- Dependencies: 167 1946 179 1960
-- Name: bet_subscriber_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY bet
    ADD CONSTRAINT bet_subscriber_fk FOREIGN KEY (idsubscriber) REFERENCES subscriber(idsubscriber);


--
-- TOC entry 1955 (class 2606 OID 17372)
-- Dependencies: 1936 169 172 1960
-- Name: competitionparticipants_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants
    ADD CONSTRAINT competitionparticipants_competition_fk FOREIGN KEY (idcompetition) REFERENCES competition(idcompetition);


--
-- TOC entry 1956 (class 2606 OID 17377)
-- Dependencies: 1944 177 172 1960
-- Name: competitionparticipants_competitor_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionparticipants
    ADD CONSTRAINT competitionparticipants_competitor_fk FOREIGN KEY (idcompetitor) REFERENCES competitor(idcompetitor);


--
-- TOC entry 1957 (class 2606 OID 17382)
-- Dependencies: 1936 169 175 1960
-- Name: competitorranking_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking
    ADD CONSTRAINT competitorranking_competition_fk FOREIGN KEY (idcompetition) REFERENCES competition(idcompetition);


--
-- TOC entry 1958 (class 2606 OID 17387)
-- Dependencies: 1944 177 175 1960
-- Name: competitorranking_competitor_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY competitionranking
    ADD CONSTRAINT competitorranking_competitor_fk FOREIGN KEY (idcompetitor) REFERENCES competitor(idcompetitor);


-- Completed on 2014-05-19 11:40:42 CEST

--
-- PostgreSQL database dump complete
--

