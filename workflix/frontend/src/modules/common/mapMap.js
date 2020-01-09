// @flow

export function mapMap<K, V> (m: Map<K, V>, mapper: (K, V) => V): Map<K, V> {
  return new Map<K, V>(Array.from(m.entries()).map(([k, v]) => [k, mapper(k, v)]))
}
