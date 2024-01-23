describe('Edit-Card-List Component', () => {

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

    cy.intercept('GET', '/api/v1/decks/1/cards', {
      statusCode: 200,
      body: [{
        id: 1,
        question: "A test question",
        type: "BASIC"
      }]
    }).as("loadDetails")

    cy.visit('http://localhost:4200/deck/1/edit');
  });


  it('Card-List: should load Cards', () => {
    cy.wait("@loadDetails").then((interception) => {

      cy.get('#card-question').should("include.text", "A test question")
      cy.get('#type-of-card').should("include.text", "Basic Card")
    });
  });

  it('Card-List: should delete card', () => {
    cy.intercept('DELETE', "/api/v1/decks/1/cards/1", {
      statusCode: 200
    })
    cy.intercept('GET', '/api/v1/decks/1/cards', {
      statusCode: 200,
      body: []
    })

    cy.get('#toggle-menu').click()
    cy.get('#delete-card-button').click()

    cy.get('.delete-button').click()
    cy.get('#card-question').should("not.exist")
    cy.get('#type-of-card').should("not.exist")

  });

  it('Card-List: should, on create first card, reach edit card view', () => {
    cy.intercept('GET', '/api/v1/decks/1/cards', {
      statusCode: 200,
      body: []
    })
    cy.reload();

    cy.get('#first-card-button').click()
    cy.url().should("include", '/deck/1/card/new/edit')
  });

  it('Card-List: should, on create card, reach edit card view', () => {
    cy.get('#toggle-menu').click()
    cy.get('#create-card-button').click()

    cy.url().should("include","/deck/1/card/new/edit")
  });

  it('Card-List: should, pressing on card, reach edit card view', () => {
    cy.intercept('GET',"/api/v1/tutorials/EDIT_DECK")
    cy.intercept('GET',"/api/v1/decks/1/cards/1",{
      statusCode: 200
    })

    cy.contains("A test question").click()
    cy.url().should("include","/deck/1/card/1/edit")
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
});
