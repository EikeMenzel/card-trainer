describe('Register Component', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/register');
  });

  it('Register: should show invalid inputs error', () => {
    cy.get('#register-button').click();
    cy.get('.error').should("be.visible")
  })


  it('Register: should show invalid E-Mail ', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        status: 1,
        message: "Your Email is invalid"
      }
    }).as("invalidEmail")

    cy.get('#email').type("iAmNotValid")
    cy.get('#username').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#password-repeat').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Your Email is invalid').should("be.visible")
  });

  it('Register: should show invalid Username ', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        status: 2,
        message: "Username is to long or to short"
      }
    }).as("invalidUsername")

    cy.get('#email').type("test@test.com")
    cy.get('#username').type("Te")
    cy.get('#password').type("TestUser!1")
    cy.get('#password-repeat').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains('Username is to long or to short').should("be.visible")
  });

  it('Register: should show error when password are not equal', () => {
    cy.get('#email').type("test@test.com")
    cy.get('#username').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#password-repeat').type("TestUser1")

    cy.get('#register-button').click();

    cy.contains("Passwords do not match").should("be.visible")
  });

  it('Register: should show error for unsafe password', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 400,
      body: {
        status: 3,
        message: "Password is not safe"
      }
    }).as("invalidPassword")


    cy.get('#email').type("test@test.com")
    cy.get('#username').type("Test User")
    cy.get('#password').type("testme")
    cy.get('#password-repeat').type("testme")

    cy.get('#register-button').click();

    cy.contains("Please make sure you are using at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/")
      .should("be.visible")
  });

  it('Register: should register successfully ', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 201
    }).as("validRegistration")
    cy.get('#email').type("test@test.com")
    cy.get('#username').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#password-repeat').type("TestUser!1")

    cy.get('#register-button').click();
    cy.contains("Your registration was successful!").should("be.visible")
    cy.contains("Back to Login").click().url().should('include',"/login")
  });

  it('Register: should go back to Login ', () => {
    cy.get('.sign-in-link').click().url().should("include","/login")
  });

  it('Register: should show server not reachable ', () => {
    cy.intercept('POST','api/v1/register',{
      statusCode: 500,
    }).as("internalError")

    cy.get('#email').type("test@test.com")
    cy.get('#username').type("Test User")
    cy.get('#password').type("TestUser!1")
    cy.get('#password-repeat').type("TestUser!1")

    cy.get('#register-button').click();

    cy.contains("Server cannot be reached").should("be.visible")
  });

})
