describe('User Profile Component', () => {
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

    cy.intercept('GET', "/api/v1/tutorials/USER_PROFILE", {})
    cy.visit('http://localhost:4200/profile');
  });

  it('Profile: should display user profile information', () => {
    let username = "";
    let email = "";
    cy.wait('@userData').then((interception) => {
      username = interception.response?.body.username;
      email = interception.response?.body.email;
    });
    cy.get('#nameInput').should("have.text", username)
    cy.get('#emailInput').should("have.text", email)
  });

  it('Profile: should enter bad email', () => {
    cy.intercept("PUT", "api/v1/account", {
      statusCode: 200
    })
    cy.get('#emailInput').clear().type("invalidEmail.com")
    cy.contains("Please enter a valid email").should("be.visible")
  });

  it('Profile: should enter short username', () => {
    cy.get('#nameInput').clear().type("Use")
    cy.contains("Username too short").should("be.visible")
  });

  it('Profile: should enter long username ', () => {
    cy.get('#nameInput').clear().type("iAmAVeryLongNameForThisInputField")
    cy.contains("Username too long").should("be.visible")
  });

  it('Profile: should enter short password', () => {
    cy.get('#updatePasswordButton').click();
    cy.get('#newPassword').type('NewPass');
    cy.get('#reenterNewPassword').type('NewPass');
    cy.get('#changePwButton').click();

    cy.contains("Your password must be between 8 and 72 characters").should('be.visible');
  });

  it('Profile: should enter too Long Password', () => {
    cy.get('#updatePasswordButton').click();
    cy.get('#newPassword').type('MyNewPasswordIsReallySafeSoNoOneCanHackIntoItOrYouCanTryToCrackMyNewPassword');
    cy.get('#reenterNewPassword').type('MyNewPasswordIsReallySafeSoNoOneCanHackIntoItOrYouCanTryToCrackMyNewPassword');
    cy.get('#changePwButton').click();

    cy.contains("Your password must be between 8 and 72 characters").should('be.visible');
  });

  it('Profile: should enter not similar Passwords', () => {
    cy.get('#updatePasswordButton').click();
    cy.get('#newPassword').type('MyNewPassword!1');
    cy.get('#reenterNewPassword').type('MyNewPassword1');
    cy.get('#changePwButton').click();

    cy.contains("Passwords do not match").should('be.visible');
  });

  it('Profile: should enter no Password', () => {
    cy.get('#updatePasswordButton').click();
    cy.get('#newPassword').clear()
    cy.get('#reenterNewPassword').clear()
    cy.get('#changePwButton').click();

    cy.contains("Field cannot be empty").should('be.visible');
  });


  it('Profile: should update password', () => {
    cy.intercept("PUT", "api/v1/password", {
      statusCode: 204
    })

    cy.get('#updatePasswordButton').click();
    cy.get('#newPassword').type('NewPassword!123');
    cy.get('#reenterNewPassword').type('NewPassword!123');
    cy.get('#changePwButton').click();
    cy.contains("Password updated successfully").should('be.visible');
  });

  it('Profile: email should not be empty', () => {
    cy.get('#emailInput').clear()
    cy.contains("Field cannot be empty").should('be.visible');
  });

  it('Profile: username should not be empty', () => {
    cy.get('#nameInput').clear()
    cy.contains("Field cannot be empty").should('be.visible');
  });

  it('Profile: should save Changes', () => {
    const username = "Username"
    const email = "newEmail@email.com"
    cy.intercept('PUT', "/api/v1/account", {
      statusCode: 200,
      body: {
        username: username,
        email: email,
        achievementIds: [],
        langCode: "EN",
        cardsToLearn: 20,
        receiveLearnNotification: false
      }
    }).as('newUserData');

    cy.get('#nameInput').clear().type(username)
    cy.get('#emailInput').clear().type(email)

    cy.get('#saveProfileButton').click()
    cy.contains("Your profile has been updated successfully").should("be.visible")
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

  it('Profile: should log out', () => {
    cy.clearCookies();
    cy.get('#logoutButton').click();
    cy.on('window:confirm', () => true);
    cy.url().should('include', '/login');
  });

  it('Authorization: should throw to login', () => {
    cy.intercept('PUT', "/api/v1/account", {
      statusCode: 401}).as('unauthorizedTest');
    cy.clearCookie('card-trainer-user');
    cy.reload();
    cy.get('#saveProfileButton').click()
    cy.contains("You are not logged in. You will be sent to the login screen").should("be.visible")
    cy.url().should("include","/login")
  });

  it('Navbar: should logout ', () => {
    cy.clearCookies()
    cy.get('#dropdown-profile-logout').click()
    cy.get('#logout-dropdown').click()
    cy.on('window:confirm', () => true);
    cy.url().should("include","/login")
  });
});
