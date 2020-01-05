// @flow

const handleStringChange = (handler: string => void) =>
  (event: SyntheticInputEvent<HTMLInputElement>) => handler(event.target.value)

export default handleStringChange
