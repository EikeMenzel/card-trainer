import {Timestamp} from "rxjs";

describe('History Component', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/login');
    cy.intercept('POST', '/api/v1/login', {
      statusCode: 200
    }).as('authLoginCall');
    cy.intercept('GET', "/api/v1/account", {
      statusCode: 200,
      body: {
        username: "Test User",
        email: "test@user.com",
        achievementIds: [],
        langCode: "EN",
        cardsToLearn: 20,
        receiveLearnNotification: false
      }
    }).as('userData');
    cy.intercept('GET', "/api/v1/decks", {
      statusCode: 204
    }).as("getDecks");

    cy.get('#email')
      .type("test@user.com");
    cy.get('#password')
      .type("TestUser!1");
    cy.get("#login-button")
      .click();

    cy.intercept('GET', 'api/v1/decks/1', {
      statusCode: 200,
      body: {
        deckId: 1,
        deckName: "Test Deck",
        deckSize: 6,
        cardsToLearn: 3,
        lastLearned: new Date,
        deckLearnState: [3, 2, 0, 1, 0, 0]
      }
    }).as("loadDetails")

    cy.intercept('GET', '/api/v1/decks/1/histories', {
      statusCode: 200
    })

    cy.visit('http://localhost:4200/deck/1/histories');
  });

  it('History: should open screen', () => {
    cy.contains('You have not done any Sessions yet.').should("be.visible")
  });


  it('History: should return to deck view', () => {
    cy.get('#history-return-button').click()
    cy.url().should("include", "/deck/1")
  });

  it('History: should find new session', () => {
    cy.intercept('GET', '/api/v1/decks/1/histories', {
      statusCode: 200,
      body: [{
        historyId: 1,
        createdAt: new Date,
        status: "FINISHED",
        cardsLearned: 3
      }, {
        historyId: 2,
        createdAt: new Date,
        status: "CANCELED",
        cardsLearned: 0
      }]
    })
    cy.reload();

    cy.contains('FINISHED').should("be.visible")
    cy.contains('CANCELED').should("be.visible")
  });

  it('History: should view Modal with results', () => {
    cy.intercept('GET', '/api/v1/decks/1/histories', {
      statusCode: 200,
      body: [{
        historyId: 1,
        createdAt: new Date,
        status: "FINISHED",
        cardsLearned: 3
      }]
    });
    cy.intercept('GET', 'api/v1/decks/1/histories/1', {
      statusCode: 200,
      body: {
        historyId: 1,
        deckName: "Test Deck",
        createdAt: new Date,
        finishedAt: new Date,
        difficulty_1: 0,
        difficulty_2: 0,
        difficulty_3: 1,
        difficulty_4: 0,
        difficulty_5: 2,
        difficulty_6: 0,
        status: "FINISHED",
        cardsLearned: 2
      }
    })
    cy.reload();

    cy.contains("FINISHED").click()
    cy.contains("Cards Learned: 2").should("be.visible")
  });

  it('History: should view Modal with no cards learned', () => {
    cy.intercept('GET', '/api/v1/decks/1/histories', {
      statusCode: 200,
      body: [
        {
          historyId: 1,
          createdAt: new Date,
          status: "CANCELED",
          cardsLearned: 0
        }]
    });
    cy.intercept('GET', 'api/v1/decks/1/histories/1', {
      statusCode: 200,
      body: {
        historyId: 1,
        deckName: "Test Deck",
        createdAt: new Date,
        finishedAt: new Date,
        difficulty_1: 0,
        difficulty_2: 0,
        difficulty_3: 0,
        difficulty_4: 0,
        difficulty_5: 0,
        difficulty_6: 0,
        status: "CANCELED",
        cardsLearned: 0
      }
    })
    cy.reload();

    cy.contains("CANCELED").click()
    cy.contains("No Cards were learned during this Session").should("be.visible")
  });

  it('Navbar: should logout ', () => {
    cy.clearCookies()
    cy.get('#dropdown-profile-logout').click()
    cy.get('#logout-dropdown').click()
    cy.on('window:confirm', () => true);
    cy.url().should("include","/login")
  });

  it('Navbar: should return to Deck List View', () => {
    cy.intercept('GET', "/api/v1/account", {
      statusCode: 200,
      body: {
        username: "Test User",
        email: "test@user.com",
        achievementIds: [],
        langCode: "EN",
        cardsToLearn: 20,
        receiveLearnNotification: false
      }
    }).as('userData');
    cy.intercept('GET',"/api/v1/decks",{
      statusCode: 204
    }).as("getDecks");

    cy.get('#home-button').click()
    cy.url().should("include","/").should("not.include","login")
  });

})
