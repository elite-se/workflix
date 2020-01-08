// @flow

import type { JwtType } from '../datatypes/Token'

const TOKEN_KEY = 'auth token'

export function storeToken (token: string) {
  localStorage.setItem(TOKEN_KEY, token)
}

export function getToken (): ?string {
  return localStorage.getItem(TOKEN_KEY)
}

export function removeToken () {
  localStorage.removeItem(TOKEN_KEY)
}

export function getParsedToken (): ?JwtType {
  const token = getToken()
  if (!token) { return null }
  // eslint-disable-next-line no-magic-numbers
  const [header, payload, signature] = token.split('.', 3)
  return {
    header: JSON.parse(atob(header)),
    payload: JSON.parse(atob(payload)),
    signature: atob(signature)
  }
}

export const getCurrentUserId = () => getParsedToken()?.payload?.userId
