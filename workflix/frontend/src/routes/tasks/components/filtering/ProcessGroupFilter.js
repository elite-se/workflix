// @flow

import React from 'react'
import type { FiltersType } from '../../../../modules/datatypes/Filters'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import SimpleMultiSelect from '../../../../modules/common/components/SimpleMultiSelect'
import { uniq, without } from 'lodash'

type PropsType = {|
  filters: FiltersType,
  onFiltersChanged: (FiltersType) => void,
  processGroups: Map<number, ProcessGroupType>
|}

class ProcessGroupFilter extends React.Component<PropsType> {
  onGroupAdded = (process: ProcessGroupType) => {
    this.props.onFiltersChanged({
      ...this.props.filters,
      processGroups: uniq([...(this.props.filters.processGroups || []), process])
    })
  }

  onGroupRemoved = (process: ProcessGroupType) => {
    this.props.onFiltersChanged({
      ...this.props.filters,
      processGroups: without(this.props.filters.processGroups, process)
    })
  }

  onGroupsCleared = () => {
    this.props.onFiltersChanged({
      ...this.props.filters,
      processGroups: []
    })
  }

  getGroupTitle = (group: ProcessGroupType) => group.title
  getGroupId = (group: ProcessGroupType) => group.id

  render () {
    return <SimpleMultiSelect
      items={Array.from(this.props.processGroups.values()).filter(pg => !pg.deleted)}
      selection={this.props.filters.processGroups || []}
      render={this.getGroupTitle}
      toID={this.getGroupId}
      multiSelectProps={{
        placeholder: 'Filter process group…'
      }}
     onItemAdded={this.onGroupAdded} onItemRemoved={this.onGroupRemoved} onItemsCleared={this.onGroupsCleared}/>
  }
}

export default ProcessGroupFilter
