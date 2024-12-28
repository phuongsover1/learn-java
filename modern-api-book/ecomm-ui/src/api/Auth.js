import Config from "./Config";

export default class Auth {
  token;
  setToken;

  constructor(argToken, argSetToken) {
    this.token = argToken;
    this.setToken = argSetToken;
    this.config = new Config();
  }

  async logInUser(credentials) {
    return fetch(this.config.LOGIN_URL, {
      method: "POST",
      mode: "cors",
      headers: {
        ...this.config.defaultHeaders(),
      },
      body: JSON.stringify(credentials),
    })
      .then((response) => Promise.all([response, response.json()]))
      .then(([response, json]) => {
        if (!response.ok) return { success: false, error: json };
        this.storeTokens(json);
        return { success: true, data: json };
      })
      .catch((e) => {
        this.handleError(e);
      });
  }

  async logoutUser(refreshToken) {
    return fetch(this.config.LOGIN_URL, {
      method: "DELETE",
      mode: "cors",
      headers: {
        ...this.config.defaultHeaders(),
      },
      body: JSON.stringify(refreshToken),
    })
      .then(this.handleReponseError.bind(this))
      .catch((e) => {
        this.handleError(e);
      });
  }

  handleReponseError(response) {
    this.clearTokens();
    if (!response.ok) {
      const error = response.json();
      console.log(error);
      throw Error(error);
    }
    return response;
  }

  handleError(error) {
    this.clearTokens();
    const err = new Map([
      [TypeError, "Can't connect to server."],
      [SyntaxError, "There was a problem parsing the response."],
      [Error, error.message],
    ]).get(error.constructor);
    console.log(err);
    return err;
  }

  storeTokens(json) {
    this.setToken(json);
    this.config.storeAccessToken(json.accessToken);
  }

  clearTokens() {
    this.config.storeAccessToken("");
    this.setToken(null);
  }
}
