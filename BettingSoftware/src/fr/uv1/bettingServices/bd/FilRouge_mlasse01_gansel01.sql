--
-- PostgreSQL database dump
--

SET statement_timeout = 0;
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
-- Name: Bet; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Bet" (
    "idBet" integer NOT NULL,
    "idSubscriber" integer,
    "idCompetition" integer,
    tokens integer,
    "idCompetitor1" integer,
    "idCompetitor2" integer,
    "idCompetitor3" integer
);


--
-- Name: Competition; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Competition" (
    "idCompetition" integer NOT NULL,
    name text,
    "closingDate" date,
    settled boolean
);


--
-- Name: CompetitionParticipants; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "CompetitionParticipants" (
    "idCompetition" integer NOT NULL,
    "idCompetitor" integer NOT NULL
);


--
-- Name: CompetitionRanking; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "CompetitionRanking" (
    "idCompetition" integer NOT NULL,
    ranking integer NOT NULL,
    "idCompetitor" integer
);


--
-- Name: Competitor; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Competitor" (
    "idCompetitor" integer NOT NULL,
    name text,
    "firstName" text,
    "birthDate" date,
    "isTeam" boolean
);


--
-- Name: Subscriber; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "Subscriber" (
    "idSubscriber" integer NOT NULL,
    username text,
    firstname text,
    lastname text,
    password text,
    tokens integer
);


--
-- Name: TeamMembers; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE "TeamMembers" (
    "idTeam" integer NOT NULL,
    "idCompetitor" integer NOT NULL
);


--
-- Name: bet_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Bet"
    ADD CONSTRAINT bet_pk PRIMARY KEY ("idBet");


--
-- Name: competition_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Competition"
    ADD CONSTRAINT competition_pk PRIMARY KEY ("idCompetition");


--
-- Name: competitionparticipants_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "CompetitionParticipants"
    ADD CONSTRAINT competitionparticipants_pk PRIMARY KEY ("idCompetition", "idCompetitor");


--
-- Name: competitor_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Competitor"
    ADD CONSTRAINT competitor_pk PRIMARY KEY ("idCompetitor");


--
-- Name: competitorranking_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "CompetitionRanking"
    ADD CONSTRAINT competitorranking_pk PRIMARY KEY ("idCompetition", ranking);


--
-- Name: subscriber_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Subscriber"
    ADD CONSTRAINT subscriber_pk PRIMARY KEY ("idSubscriber");


--
-- Name: teammembers_pk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "TeamMembers"
    ADD CONSTRAINT teammembers_pk PRIMARY KEY ("idTeam", "idCompetitor");


--
-- Name: fki_bet_competition_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_bet_competition_fk ON "Bet" USING btree ("idCompetition");


--
-- Name: fki_bet_subscriber_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_bet_subscriber_fk ON "Bet" USING btree ("idSubscriber");


--
-- Name: fki_competitionparticipants_competitor_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_competitionparticipants_competitor_fk ON "CompetitionParticipants" USING btree ("idCompetitor");


--
-- Name: fki_competitorranking_competitor_fk; Type: INDEX; Schema: public; Owner: -
--

CREATE INDEX fki_competitorranking_competitor_fk ON "CompetitionRanking" USING btree ("idCompetitor");


--
-- Name: bet_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Bet"
    ADD CONSTRAINT bet_competition_fk FOREIGN KEY ("idCompetition") REFERENCES "Competition"("idCompetition");


--
-- Name: bet_competitor1_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Bet"
    ADD CONSTRAINT bet_competitor1_fk FOREIGN KEY ("idCompetitor1") REFERENCES "Competitor"("idCompetitor");


--
-- Name: bet_competitor2_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Bet"
    ADD CONSTRAINT bet_competitor2_fk FOREIGN KEY ("idCompetitor2") REFERENCES "Competitor"("idCompetitor");


--
-- Name: bet_competitor3_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Bet"
    ADD CONSTRAINT bet_competitor3_fk FOREIGN KEY ("idCompetitor3") REFERENCES "Competitor"("idCompetitor");


--
-- Name: bet_subscriber_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "Bet"
    ADD CONSTRAINT bet_subscriber_fk FOREIGN KEY ("idSubscriber") REFERENCES "Subscriber"("idSubscriber");


--
-- Name: competitionparticipants_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "CompetitionParticipants"
    ADD CONSTRAINT competitionparticipants_competition_fk FOREIGN KEY ("idCompetition") REFERENCES "Competition"("idCompetition");


--
-- Name: competitionparticipants_competitor_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "CompetitionParticipants"
    ADD CONSTRAINT competitionparticipants_competitor_fk FOREIGN KEY ("idCompetitor") REFERENCES "Competitor"("idCompetitor");


--
-- Name: competitorranking_competition_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "CompetitionRanking"
    ADD CONSTRAINT competitorranking_competition_fk FOREIGN KEY ("idCompetition") REFERENCES "Competition"("idCompetition");


--
-- Name: competitorranking_competitor_fk; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY "CompetitionRanking"
    ADD CONSTRAINT competitorranking_competitor_fk FOREIGN KEY ("idCompetitor") REFERENCES "Competitor"("idCompetitor");


--
-- PostgreSQL database dump complete
--

