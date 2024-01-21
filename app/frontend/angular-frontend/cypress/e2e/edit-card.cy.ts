describe('Edit-Card-Component: create New Card', () => {
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

    cy.intercept('GET', '/api/v1/tutorials/EDIT_DECK', {})

    cy.visit('http://localhost:4200/deck/1/card/new/edit');
  });

  it('Edit-Card: should return to card lists ', () => {
    cy.get('#return-to-card-list').click()
    cy.url().should("include", "/deck/1/edit")
  });

  it('Edit-Card: should open new card', () => {
    cy.get('#basic-card-question').should("not.have.text")
    cy.get('#basic-card-answer').should('not.have.text')
  });

  it('Edit-Card: should open image Modal for question', () => {
    cy.get('#image-modal-question').click()
    cy.contains("Upload Image").should("be.visible")
  });

  it('Edit-Card: should open image Modal for answer', () => {
    cy.get('#image-modal-answer').click()
    cy.contains("Upload Image").should("be.visible")
  });

  it('Edit-Card: on save Card, should throw error', () => {
    cy.get('#save-card-button').click()
    cy.contains("Please make sure you have entered an answer in every possibility.").should('be.visible')
  });

  it('Edit-Card: should save card', () => {
    cy.intercept('POST', "/api/v1/decks/1/cards", {
      statusCode: 201
    })

    cy.get('#basic-card-question').type("I am a question")
    cy.get('#basic-card-answer').type("I am an answer")
    cy.get('#save-card-button').click()

    cy.contains("Your card has been created").should("be.visible")
    cy.url().should("include", "/deck/1/edit")
  });

  it('Edit-Card: should open Multiple Choice Card', () => {
    cy.get('#cardTypeSelect').select("multipleChoice")
    cy.contains("Option 1").should("be.visible")
    cy.contains("Option 2").should("be.visible")
  });

  it('Edit-Card: should create new Answer in multiple Choice', () => {
    cy.get('#cardTypeSelect').select("multipleChoice")
    cy.get('#add-multiple-answers').click()

    cy.contains("Option 3").should("be.visible")
  });

  it('Edit-Card: should create new Answer in multiple Choice', () => {
    cy.get('#cardTypeSelect').select("multipleChoice")
    cy.get('#add-multiple-answers').click()

    cy.get('#delete-answer-multiple').click()
    cy.contains("Option 3").should("not.exist")
  })

  it('Navbar: should logout ', () => {
    cy.clearCookies()
    cy.get('#dropdown-profile-logout').click({force: true})
    cy.get('#logout-dropdown').click({force: true})
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


describe('Edit-Card-Component: Edit existent Card', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/login');
    cy.intercept('POST', '/api/v1/login', {
      statusCode: 200
    }).as('authLoginCall');
    cy.intercept('GET', "/api/v1/account", {
      statusCode: 200,
      body: {
        username: "Test User2",
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

    cy.intercept('GET', '/api/v1/decks/1/cards/1', {
      statusCode: 200,
      body: {
        cardDTO: {
          id: 1,
          question: "A test question",
          imageId: null,
          cardType: "BASIC"
        },
        textAnswerCardId: 1,
        textAnswer: "A test Answer",
        imageId: null
      }
    }).as("loadDetails")

    cy.intercept('GET', "/api/v1/tutorials/EDIT_DECK", {})

    cy.visit('http://localhost:4200/deck/1/card/1/edit');

  });

  it('Edit-Deck: should open edit with card details', () => {
    cy.get('#basic-card-question').should("have.value", "A test question")
    cy.get('#basic-card-answer').should("have.value", "A test Answer")
  });

  it('Edit-Deck: should open multiple-choice-card with details ', () => {
    cy.intercept('GET', '/api/v1/decks/1/cards/1', {
      statusCode: 200,
      body: {
        cardDTO: {
          question: "A test Question",
          imageId: null
        },
        choiceAnswers: [{
          choiceAnswerId: 1,
          answer: "A test Answer",
          rightAnswer: false,
          imageId: null
        }, {
          choiceAnswerId: 2,
          answer: "A second test Answer",
          rightAnswer: true,
          imageId: null
        }]
      }
    }).as("loadDetails")

    cy.reload()

    cy.get("#multiple-question-card").should("have.value","A test Question")
    cy.get('#options-multi-card0').should("have.value","A test Answer")
    cy.get('#options-multi-card1').should("have.value","A second test Answer")
  });

  it('Edit-Card: should return to card lists ', () => {
    cy.get('#return-to-card-list').click()
    cy.url().should("include", "/deck/1/edit")
  });

  it('Edit-Card: on save Card, should throw error', () => {
    cy.get('#basic-card-question').clear()
    cy.get('#basic-card-answer').clear()

    cy.get('#save-card-button').click()
    cy.contains("Please make sure you have entered an answer in every possibility.").should('be.visible')
  });

  it('Edit-Card: should safe card', () => {
    cy.intercept('PUT', "/api/v1/decks/1/cards/1", {
      statusCode: 204
    })
    cy.get('#save-card-button').click()

    cy.contains("Your card has been updated").should("be.visible")
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
        username: "Test User2",
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
