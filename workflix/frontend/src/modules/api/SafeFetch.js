// @flow

export const safeFetch = (input: RequestInfo, init?: RequestOptions) => fetch(input, init).then(response => {
  if (response.ok) { return response } else { throw response }
})
