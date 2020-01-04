// @flow

import React from 'react'
import StateFilter from './StateFilter'
import styled from 'styled-components'
import InvolvingFilter from './InvolvingFilter'
import type { UserType } from '../../../../modules/datatypes/User'
import type { FiltersType } from '../../../../modules/datatypes/Filters'

type PropsType = {|
  filters: FiltersType,
  onFiltersChanged: (FiltersType) => void,
  users: Map<string, UserType>
|}

const FiltersContainer = styled<{}, {}, 'div'>('div')`
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: flex-end;
  align-items: center;
  
  margin: 5px;
  > * {
    display: block;
    margin: 0 5px;
  }
  > *:first-child {
    margin-left: 0
  }
  > *:last-child {
    margin-right: 0
  }
`

class Filters extends React.Component<PropsType> {
  render () {
    return <FiltersContainer>
      <StateFilter filters={this.props.filters} onFiltersChanged={this.props.onFiltersChanged}/>
      <InvolvingFilter
        filters={this.props.filters}
        onFiltersChanged={this.props.onFiltersChanged}
        users={this.props.users}
      />
    </FiltersContainer>
  }
}

export default Filters
