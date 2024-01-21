describe('Deck-Detail Component', () => {
beforeEach(() => {
  cy.visit('http://localhost:4200/login');
  cy.intercept('POST', '/api/v1/login',{
    statusCode: 200
  }).as('authLoginCall');
  cy.intercept('GET', "/api/v1/account",{
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

  cy.get('#email')
    .type("test@user.com");
  cy.get('#password')
    .type("TestUser!1");
  cy.get("#login-button")
    .click();

  cy.intercept('GET','api/v1/decks/1',{
    statusCode: 200,
    body: {
      deckId: 1,
      deckName: "Test Deck",
      deckSize: 6,
      cardsToLearn: 3,
      lastLearned: new Date,
      deckLearnState: [3,2,0,1,0,0]
    }
  }).as("loadDetails")

  cy.intercept('GET', "/api/v1/tutorials/DECK_VIEW", {})
  cy.visit('http://localhost:4200/deck/1');
});


it('Deck-Detail: should open Page', () => {

  cy.wait("@loadDetails").then((interception) => {
    const title = interception.response?.body.deckName;
    const deckSize = interception.response?.body.deckSize;
    const cardsToLearn = interception.response?.body.cardsToLearn;


    cy.get('.title-text').should("have.text", title)
    cy.get('#deck-size').should("have.text", deckSize)
    cy.get('#cards-left').should("have.text", cardsToLearn)

  });
})

  it('Deck-Detail: should open share modal', () => {
    cy.get('#share-modal-button').click()

    cy.contains('Share your Deck').should("be.visible")
    cy.contains('Enter the E-Mail Address of the recipient:').should("be.visible")
  });

  it('Deck-Detail: should show error for bad Email', () => {
    cy.intercept('POST','api/v1/decks/1/share',{
      statusCode:404
    })
    cy.get('#share-modal-button').click()

    cy.get('#email-holder').type("iamNotValid")
    cy.get('#share-deck-button').click()

    cy.contains("User or Deck not found").should("be.visible")
  });

  it('Deck-Detail: should show error for no Email', () => {
    cy.get('#share-modal-button').click()

    cy.get('#email-holder').clear()
    cy.get('#share-deck-button').click()

    cy.contains("Please enter a valid email").should("be.visible")
  });

  it('Deck-Detail: should send Deck successfully', () => {
    cy.intercept('POST','api/v1/decks/1/share',{
      statusCode:200
    })
    cy.get('#share-modal-button').click()

    cy.get('#email-holder').type("test@email.com")
    cy.get('#share-deck-button').click()

    cy.contains("The deck was sent").should("be.visible")
  });

  it('Deck-Detail: should open edit card view', () => {
    cy.intercept('GET','/api/v1/decks/1/cards',{
      statusCode: 200
    })
    cy.get('#edit-card-button').click()
    cy.url().should("include","/deck/1/edit")
  });

  it('Deck-Detail: should open history view ', () => {
    cy.intercept('GET','api/v1/decks/1/histories',{
      statusCode: 200
    })

    cy.get('#history-button').click()
    cy.url().should("include","/deck/1/histories")

  });

  it('Deck-Detail: should show error when no data found', () => {
    cy.intercept('GET','api/v1/decks/1',{
      statusCode: 404
    }).as("failedloadDetails")

    cy.reload()
    cy.contains("User or Deck not found").should("be.visible")
    cy.url().should("include","/")
  });

  it('Deck-Detail: should open learn session ', () => {
    cy.intercept('GET','/api/v1/tutorials/LEARN_CARD_VIEW',{})
    cy.intercept('GET','/api/v1/decks/1/cards-to-learn',{})
    cy.intercept('POST','api/v1/decks/1/learn-sessions',{
      statusCode: 200,
      body: {
        session: 1
      }
    })
    cy.intercept('GET','/api/v1/decks/1/learn-sessions/1/next-card',{
      statusCode: 200
    })

    cy.get('#start-learn-session-button').click()
    cy.url().should("include","/deck/1/learn")
  });

  it('Deck-Detail: should start peek session ', () => {
    cy.intercept('GET','/api/v1/tutorials/LEARN_CARD_VIEW',{})
    cy.intercept('GET','api/v1/decks/1/size',{})
    cy.intercept('POST','api/v1/decks/1/peek-sessions',{
      statusCode: 200,
      body: {
        session: 1
      }
    })
    cy.intercept('GET','/api/v1/peek-sessions/1/next-card',{
      statusCode: 200
    })

    cy.get('#start-peek-session-button').click()
    cy.url().should("include","/deck/1/peek")
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
