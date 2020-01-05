// @flow

import { getToken } from '../common/tokenStorage'

const AUTH_HEADER = 'Authorization'

type MyOptionsType = {
  ...$Diff<RequestOptions, { headers?: HeadersInit }>,
  headers?: { [key: string]: string }
}

const addTokenHeader = (init?: MyOptionsType): MyOptionsType => {
  const token = getToken()
  if (!token) {
    return init || {}
  }
  if (!init || !init.headers) {
    return { ...init, headers: { [AUTH_HEADER]: token } }
  }
  const headers: { [key: string]: string } = init.headers
  return {
    ...init,
    headers: {
      ...headers,
      [AUTH_HEADER]: token
    }
  }
}

export const safeFetch = async (input: RequestInfo, init?: RequestOptions) => {
  const response = await fetch(input, addTokenHeader(init))
  if (!response.ok) {
    const text = await response.text()
    throw new Error(text)
  }
  return response
}
