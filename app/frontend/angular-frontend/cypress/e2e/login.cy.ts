describe('Login Component', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/login');
  });

  it('Login: should login successfully', () => {
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
        cardsToLearn: 0,
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

    cy.url().should("include","/").should("not.include","login")
  });

  it('Login: should not find account', () => {
    cy.intercept('POST', '/api/v1/login',{
      statusCode: 401
    }).as('authLoginCall');

    cy.get('#email')
      .type("test@user.com");
    cy.get('#password')
      .type("TestUser!1");
    cy.get("#login-button")
      .click();

    cy.contains("Wrong Credentials or the user isn't verified")
      .should("be.visible")
  });

  it('Login: should not connect', () => {
    cy.intercept('POST', '/api/v1/login',{
      statusCode: 500
    }).as('authLoginCall');

    cy.get('#email')
      .type("test@user.com");
    cy.get('#password')
      .type("TestUser!1");
    cy.get("#login-button")
      .click();

    cy.contains("The Login Service is currently unavailable")
      .should("be.visible")
  });

  it('Login: should enter invalid e-mail', () => {
    cy.get('#email')
      .type("testMeNotValid");

    cy.get("#login-button")
      .click();

    cy.contains("Please enter a valid E-Mail and Password").should("be.visible");
  });

  it('Login: should enter invalid password', () => {
    cy.get('#password')
      .type("Testpassword");

    cy.get("#login-button")
      .click();

    cy.contains("Please enter a valid E-Mail and Password").should("be.visible");
  });

  it('Login: should visit register Screen', () => {
    cy.contains("Sign up").click()
    cy.url().should("include","/register")
  });

  it('Login: should open Password Reset Screen', () => {
    cy.contains("Forgot Password?").click()
    cy.url().should("include","/forgot-password")
  });
})
