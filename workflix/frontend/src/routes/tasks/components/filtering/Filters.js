// @flow

import React from 'react'
import type { FiltersType } from '../../types/Filters'
import StateFilter from './StateFilter'

type PropsType = {|
  filters: FiltersType,
  onFiltersChanged: (FiltersType) => void
|}

class Filters extends React.Component<PropsType> {
  render () {
    return <div>
      <StateFilter filters={this.props.filters} onFiltersChanged={this.props.onFiltersChanged}/>
    </div>
  }
}

export default Filters
