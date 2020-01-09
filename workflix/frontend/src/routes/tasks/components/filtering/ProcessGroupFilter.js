// @flow

import React from 'react'
import type { FiltersType } from '../../../../modules/datatypes/Filters'
import type { ProcessGroupType } from '../../../../modules/datatypes/ProcessGroup'
import SimpleMultiSelect from '../../../../modules/common/components/SimpleMultiSelect'

type PropsType = {|
  filters: FiltersType,
  onFiltersChanged: (FiltersType) => void,
  processGroups: Map<number, ProcessGroupType>
|}

class ProcessGroupFilter extends React.Component<PropsType> {
  onGroupsUpdated = (processGroups: ProcessGroupType[]) =>
    this.props.onFiltersChanged({
      ...this.props.filters,
      processGroups
    })

  getGroupTitle = (group: ProcessGroupType) => group.title
  getGroupId = (group: ProcessGroupType) => group.id

  render () {
    return <SimpleMultiSelect
      items={Array.from(this.props.processGroups.values())}
      selection={this.props.filters.processGroups || []}
      onSelectionChanged={this.onGroupsUpdated}
      render={this.getGroupTitle}
      toID={this.getGroupId}
      multiSelectProps={{
        placeholder: 'Filter process group…'
      }}
    />
  }
}

export default ProcessGroupFilter
