// @flow

export type JwtHeaderType = {
  alg: string,
  typ: string
}

export type JwtPayloadType = {
  userId: string
}

export type JwtType = {
  header: JwtHeaderType,
  payload: JwtPayloadType,
  signature: string
}
