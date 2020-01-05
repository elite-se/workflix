// @flow

const TOKEN_KEY = 'auth token'

export function storeToken (token: string) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function getToken (): ?string {
  return localStorage.getItem(TOKEN_KEY)
}
