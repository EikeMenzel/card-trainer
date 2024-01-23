import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';

// register.component.spec.ts

describe('RegisterComponent', () => {
  beforeEach(() => {
    cy.visit('/register');
  });

  it('should display the register form', () => {
    cy.get('.register-container').should('exist');
    cy.get('.card').should('exist');
    cy.get('#register-form').should('exist');
  });

  it('should display the form fields', () => {
    cy.get('#email').should('exist');
    cy.get('#username').should('exist');
    cy.get('#password').should('exist');
    cy.get('#password-repeat').should('exist');
  });

  it('should allow user input in the form', () => {
    const email = 'test@example.com';
    const username = 'testuser';
    const password = 'testpassword';

    cy.get('#email').type(email).should('have.value', email);
    cy.get('#username').type(username).should('have.value', username);
    cy.get('#password').type(password).should('have.value', password);
    cy.get('#password-repeat').type(password).should('have.value', password);
  });

  it('should validate email format', () => {
    // Assuming you have error messages next to the input fields
    cy.get('#email').type('invalid-email').blur();
    cy.get('.error').contains('Please enter a valid email.').should('exist');
  });

  it('should validate required fields', () => {
    // Assuming you have error messages next to the input fields
    cy.get('#username').type('testuser').clear().blur();
    cy.get('.error').contains('Username is required.').should('exist');

    cy.get('#password').type('testpassword').clear().blur();
    cy.get('.error').contains('Password is required.').should('exist');
  });

  it('should successfully submit the form', () => {
    const email = 'test@example.com';
    const username = 'testuser';
    const password = 'testpassword';

    cy.get('#email').type(email);
    cy.get('#username').type(username);
    cy.get('#password').type(password);
    cy.get('#password-repeat').type(password);

    cy.get('.submit-button').click();

    // Assuming you have a success message or a route change after successful registration
    cy.url().should('include', '/success-route');
  });
});

