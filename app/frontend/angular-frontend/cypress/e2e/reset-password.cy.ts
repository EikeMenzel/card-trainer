describe('template spec', () => {
  beforeEach(() => {
    cy.visit('http://localhost:4200/reset-password');
  });

  it('Reset-Password: should throw error on no Password', () => {
    cy.get('#newPassword').clear()
    cy.get('#reenterNewPassword').clear()

    cy.get('#submit-new-password').click()
    cy.contains("Field cannot be empty").should("be.visible")
  });

  it('Reset-Password: should throw error on short password', () => {
    cy.get('#newPassword').type("test")
    cy.get('#reenterNewPassword').type("test")

    cy.get('#submit-new-password').click()
    cy.contains("Password must be at least 8 characters long").should("be.visible")
  });

  it('Reset-Password: should throw error on long password', () => {
    cy.get('#newPassword').type("MyNewPasswordIsReallySafeSoNoOneCanHackIntoItOrYouCanTryToCrackMyNewPassword")
    cy.get('#reenterNewPassword').type("MyNewPasswordIsReallySafeSoNoOneCanHackIntoItOrYouCanTryToCrackMyNewPassword")

    cy.get('#submit-new-password').click()
    cy.contains("Password can not be longer than 72 characters").should("be.visible")
  });

  it('Reset-Password: should throw error on bad password', () => {
    cy.intercept('PUT', "/api/v1/password/reset", {
      statusCode: 400,
      body: {
        status: 3
      }
    })

    cy.get('#newPassword').type("IamNotSafe")
    cy.get('#reenterNewPassword').type("IamNotSafe")

    cy.get('#submit-new-password').click()
    cy.contains("Please make sure you are using at least 1x digit, 1x capitalized and 1x lower-case letter and at least 1x symbol from the following pool: ~`! @#$%^&*()_-+={[}]|:;<,>.?/").should("be.visible")
  });

  it('Reset-Password: should throw error on not equal passwords', () => {
    cy.get('#newPassword').type("MyNewPassword!1")
    cy.get('#reenterNewPassword').type("MyNewPassword!")

    cy.get('#submit-new-password').click()
    cy.contains("Passwords do not match").should("be.visible")
  });

  it('Reset-Password: should throw error on bad request', () => {
    cy.intercept('PUT', "/api/v1/password/reset", {
      statusCode: 400,
      body: {
        status: 5
      }
    })

    cy.get('#newPassword').type("IamNotSafe")
    cy.get('#reenterNewPassword').type("IamNotSafe")

    cy.get('#submit-new-password').click()
    cy.contains("The reset-token is invalid, expired or already used. Please request another password reset").should("be.visible")
  });

  it('Reset-Password: should throw success and back to login', () => {
    cy.intercept('PUT', "/api/v1/password/reset", {
      statusCode: 200,
    })

    cy.get('#newPassword').type("MyNewPassword!1")
    cy.get('#reenterNewPassword').type("MyNewPassword!1")

    cy.get('#submit-new-password').click()
    cy.contains("Your password reset was a success Have fun with your account and happy learning ❤️").should("be.visible")
    cy.url().should("include","/login")
  });

})
