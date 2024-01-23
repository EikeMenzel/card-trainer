describe('Deck-List Component', () => {

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
  });

  it('Deck-List: should create first Deck', () => {
    cy.intercept('POST', "api/v1/decks", {
      statusCode: 201
    }).as("sendDeck")

    cy.intercept('GET', 'api/v1/decks', {
      statusCode: 200,
      body: [{
        deckId: 1,
        deckName: "Test Deck",
        deckSize: 0,
        cardsToLearn: 0,
        lastLearned: new Date()
      }]
    })

    cy.get('#first-time-add').click()
    cy.get('#add-new-item-field').type("Test Deck")
    cy.get('#form-add-new-field').submit()

    cy.contains("Test Deck").should("be.visible")
  });

  it('Deck-List: leave empty name', () => {
    cy.intercept('POST', "api/v1/decks", {
      statusCode: 201
    }).as("sendDeck")

    cy.get('#first-time-add').click()
    cy.get('#add-new-item-field').clear()
    cy.get('#form-add-new-field').submit()

    cy.contains("Error").should("be.visible")
    cy.contains("Deck can't be empty").should("be.visible")
  });

  it('Deck-List: should open import Modal', () => {
    cy.get('#toggle-options').click()
    cy.get('#import-button').click()
    cy.contains("Import your deck").should("be.visible")
  });

  it('Deck-List: should add another Deck', () => {
    cy.intercept('POST', "api/v1/decks", {
      statusCode: 201
    }).as("sendDeck")

    cy.intercept('GET', 'api/v1/decks', {
      statusCode: 200,
      body: [{
        deckId: 1,
        deckName: "Test Deck",
        deckSize: 0,
        cardsToLearn: 0,
        lastLearned: new Date()
      }]
    })

    cy.get('#first-time-add').click()
    cy.get('#add-new-item-field').type("Test Deck")
    cy.get('#form-add-new-field').submit()

    cy.intercept('GET', 'api/v1/decks', {
      statusCode: 200,
      body: [{
        deckId: 1,
        deckName: "Test Deck",
        deckSize: 0,
        cardsToLearn: 0,
        lastLearned: new Date()
      }, {
        deckId: 1,
        deckName: "Test Deck2",
        deckSize: 0,
        cardsToLearn: 0,
        lastLearned: new Date()
      }]
    })

    cy.get('#toggle-options').click()
    cy.get('#add-deck-button').click()
    cy.get('#form-add-new-field').type("Test Deck2")
    cy.get('#form-add-new-field').submit()
  });

  it('Deck-List: should open deck details view', () => {
    cy.intercept('POST', "api/v1/decks", {
      statusCode: 201
    }).as("sendDeck")

    cy.intercept('GET', 'api/v1/decks', {
      statusCode: 200,
      body: [{
        deckId: 1,
        deckName: "Test Deck",
        deckSize: 0,
        cardsToLearn: 0,
        lastLearned: new Date()
      }]
    })

    cy.reload()
    cy.contains("Test Deck").click()

    cy.url().should('include','deck/1')
  });

  it('Deck-List: should delete Deck ', () => {
    cy.intercept('POST', "api/v1/decks", {
      statusCode: 201
    }).as("sendDeck")

    cy.intercept('GET', 'api/v1/decks', {
      statusCode: 200,
      body: [{
        deckId: 1,
        deckName: "Test Deck",
        deckSize: 0,
        cardsToLearn: 0,
        lastLearned: new Date()
      }]
    })

    cy.intercept('DELETE','api/v1/decks/1',{
      statusCode: 204
    })

    cy.reload()

    cy.intercept('GET', 'api/v1/decks', {
      statusCode: 200,
      body: []
    })

    cy.get('#toggle-options').click()
    cy.get('#toggle-delete-button').click()

    cy.get('#trigger-delete-button').click()
    cy.on('window:confirm', () => {return true});

    cy.contains("Test Deck").should("not.exist")
  });

  it('Navbar: should logout ', () => {
    cy.clearCookies()
    cy.get('#dropdown-profile-logout').click()
    cy.get('#logout-dropdown').click()
    cy.on('window:confirm', () => true);
    cy.url().should("include","/login")
  });

})
