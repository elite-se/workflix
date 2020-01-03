// @flow

import React from 'react'

/**
 * Taken from https://github.com/palantir/blueprint/blob/develop/packages/docs-app/src/examples/select-examples/films.tsx
 */
function highlightText (text: string, query: string): React$Node {
  let lastIndex = 0
  const words = query
    .split(/\s+/)
    .filter(word => word.length > 0)
    .map(text => text.replace(/([.*+?^=!:${}()|[\]/\\])/g, '\\$1'))
  if (words.length === 0) {
    return [text]
  }
  const regexp = new RegExp(words.join('|'), 'gi')
  const tokens: React$Node[] = []
  while (true) {
    const match = regexp.exec(text)
    if (!match) {
      break
    }
    const length = match[0].length
    const before = text.slice(lastIndex, regexp.lastIndex - length)
    if (before.length > 0) {
      tokens.push(before)
    }
    lastIndex = regexp.lastIndex
    tokens.push(<strong key={lastIndex}>{match[0]}</strong>)
  }
  const rest = text.slice(lastIndex)
  if (rest.length > 0) {
    tokens.push(rest)
  }
  return tokens
}

export default highlightText
