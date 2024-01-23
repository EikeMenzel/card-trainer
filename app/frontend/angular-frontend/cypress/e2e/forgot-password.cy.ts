describe('Forgot Password Component', () => {

  beforeEach(() => {
    cy.visit('http://localhost:4200/forgot-password');
  });

  it('Forgot Password: should open screen', () => {
    cy.contains('Reset your password').should('be.visible')
  });

  it('Forgot Password: should show error on bad email', () => {
    cy.get('#email').type("invalidEmail")
    cy.get('#submit-email').click()
    cy.contains("Please enter a valid email").should("be.visible")
  });

  it('Forgot Password: should show error on no email', () => {
    cy.get('#email').clear()
    cy.get('#submit-email').click()
    cy.contains("Field cannot be empty").should("be.visible")
  });

  it('Forgot Password: should send Email', () => {
    cy.intercept('POST','api/v1/password/reset',{
      statusCode: 202
    })

    cy.get('#email').type("test@email.com")
    cy.get('#submit-email').click()

    cy.contains("We sent you an email to test@email.com. Please follow the steps provided in the email to reset your password. Sending the E-Mail could take a while.")
  });

})
