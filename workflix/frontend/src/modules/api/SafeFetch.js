// @flow

export const safeFetch = async (input: RequestInfo, init?: RequestOptions) => {
  const response = await fetch(input, init)
  if (!response.ok) {
    const text = await response.text()
    throw new Error(text)
  }
  return response
}
